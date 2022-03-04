package de.pse.kit.studywithme.model.repository

import android.content.Context
import android.util.Log
import de.pse.kit.studywithme.SingletonHolder
import de.pse.kit.studywithme.model.data.Institution
import de.pse.kit.studywithme.model.data.Major
import de.pse.kit.studywithme.model.data.User
import de.pse.kit.studywithme.model.database.AppDatabase
import de.pse.kit.studywithme.model.network.UserService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * User repository
 *
 * @constructor
 *
 * @param context
 */
class UserRepository private constructor(context: Context) : UserRepositoryInterface {
    private val userService = UserService.instance
    private val userDao = AppDatabase.getInstance(context).userDao()
    private val auth = Authenticator

    @ExperimentalCoroutinesApi
    override suspend fun isSignedIn(): Boolean {
        if (auth.firebaseUID == null) {
            Log.d(auth.TAG, "no user is signed in")
            return false
        }
        Log.d(auth.TAG, "user is signed in")
        getSignedInUser().collect()
        return true
    }

    @ExperimentalCoroutinesApi
    override suspend fun getSignedInUser(): Flow<User> = channelFlow {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        val truthWasSend = AtomicBoolean(false)
        send(auth.user)

        launch {
            val remoteUserLight = userService.getUser(auth.firebaseUID!!)
            if (remoteUserLight != null) {
                val remoteUser = userService.getUser(remoteUserLight.userID.toInt())
                auth.user = remoteUser ?: auth.user
            }

            send(auth.user)
            truthWasSend.set(true)
        }
        launch {
            val localUser = userDao.getUser(auth.firebaseUID!!)
            if (!truthWasSend.get()) {
                auth.user = localUser
                send(localUser)
            }
        }
    }.filterNotNull()

    override suspend fun editSignedInUser(user: User): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }
        val remoteUser = userService.editUser(user.userID, user)

        if (remoteUser == user) {
            userDao.editUser(user)
            auth.user = user

            return true
        } else {
            return false
        }
    }

    override suspend fun signIn(email: String, password: String): Boolean {
        if (auth.signIn(email, password)) {
            val remoteUserLight = userService.getUser(auth.firebaseUID!!)
            if (remoteUserLight != null) {
                val remoteUser = userService.getUser(remoteUserLight.userID.toInt())
                if (remoteUser != null) {
                    Log.d(auth.TAG, "Remote Database User Get:success")
                    userDao.saveUser(remoteUser)

                    auth.user = remoteUser

                    return true
                }
            }
            return false
        } else {
            return false
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        username: String,
        major: String,
        institution: String
    ): Boolean {
        if (!auth.signUp(email, password)) {
            return false
        }
        val remoteInstitution = getCollege(institution)
        val remoteMajor = getMajor(major)

        if (remoteInstitution == null || remoteMajor == null) {
            auth.deleteFirebaseUser(email, password)
            return false
        }

        val user = User(
            userID = -1,
            name = username,
            contact = email,
            college = remoteInstitution.name,
            collegeID = remoteInstitution.institutionID.toInt(),
            major = remoteMajor.name,
            majorID = remoteMajor.majorID.toInt(),
            firebaseUID = auth.firebaseUID ?: "error"
        )

        Log.d(auth.TAG, user.toString() + "local")
        val remoteUser = userService.newUser(user)
        Log.d(auth.TAG, remoteUser.toString() + "remote")
        if (remoteUser != null) {
            Log.d(auth.TAG, "Remote Database User Post:success")
            userDao.saveUser(remoteUser)
            auth.user = remoteUser

            return true
        } else {
            auth.deleteFirebaseUser(email, password)
            return false
        }
    }

    override suspend fun resetPassword(email: String): Boolean {
        return auth.resetPassword(email)
    }

    override suspend fun signOut(): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        //TODO: Maye delete all local data
        auth.signOut()
        if (auth.firebaseUID == null) {
            return true
        }
        return false
    }

    @ExperimentalCoroutinesApi
    override suspend fun deleteAccount(email: String, password: String): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        // TODO: Delete all local data
        getSignedInUser().collect() {
            userService.removeUser(it.userID)
        }
        return auth.deleteFirebaseUser(email, password)
    }

    override suspend fun getMajors(prefix: String): List<String> {
        return userService.getMajors(prefix)?.map {
            it.name
        } ?: emptyList()
    }

    override suspend fun getMajor(name: String): Major? {
        val majors = userService.getMajors(name)
        if (majors?.map {
                it.name
            }?.contains(name) != true) {
            return userService.newMajor(Major(-1, name))
        } else {
            return majors.last {
                it.name == name
            }
        }
    }

    override suspend fun getColleges(prefix: String): List<String> {
        return userService.getColleges(prefix)?.map {
            it.name
        } ?: emptyList()
    }

    override suspend fun getCollege(name: String): Institution? {
        val colleges = userService.getColleges(name)
        if (colleges?.map {
                it.name
            }?.contains(name) != true) {
            return userService.newCollege(Institution(-1, name))
        } else {
            return colleges.last {
                it.name == name
            }
        }
    }

    companion object : SingletonHolder<UserRepository, Context>({ UserRepository(it) })
}