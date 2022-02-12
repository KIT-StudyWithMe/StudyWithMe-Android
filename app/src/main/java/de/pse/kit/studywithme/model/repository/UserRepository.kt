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
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicBoolean

class UserRepository private constructor(context: Context) : UserRepositoryInterface {
    private val userService = UserService.instance
    private val userDao = AppDatabase.getInstance(context).userDao()
    private val auth = Authenticator
    private var userCache: User? = null

    @ExperimentalCoroutinesApi
    override fun isSignedIn(): Boolean {
        if (auth.firebaseUID == null) {
            Log.d(auth.TAG, "no user is signed in")
            return false
        }
        Log.d(auth.TAG, "user is signed in")
        return runBlocking {
            getSignedInUser().collect()
            return@runBlocking true
        }
    }

    @ExperimentalCoroutinesApi
    override fun getSignedInUser(): Flow<User> {

        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return channelFlow {
            val truthWasSend = AtomicBoolean(false)
            send(userCache)

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
    }

    override fun editSignedInUser(user: User): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return runBlocking {
            val remoteUser = userService.editUser(user.userID, user)

            if (remoteUser == user) {
                userDao.editUser(user)
                userCache = user

                return@runBlocking true
            } else {
                return@runBlocking false
            }
        }
    }

    override fun signIn(email: String, password: String): Boolean {
        return runBlocking {
            if (auth.signIn(email, password)) {
                val remoteUserLight = userService.getUser(auth.firebaseUID!!)
                if (remoteUserLight != null) {
                    val remoteUser = userService.getUser(remoteUserLight.userID.toInt())
                    if (remoteUser != null) {
                        Log.d(auth.TAG, "Remote Database User Get:success")
                        userDao.saveUser(remoteUser)

                        userCache = remoteUser
                        auth.user = remoteUser

                        return@runBlocking true
                    }
                }
                return@runBlocking false
            } else {
                return@runBlocking false
            }
        }
    }

    override fun signUp(
        email: String,
        password: String,
        username: String,
        major: String,
        institution: String
    ): Boolean {
        return runBlocking {
            if (auth.signUp(email, password)) {
                val remoteInstitution = getCollege(institution)
                val remoteMajor = getMajor(major)

                if (remoteInstitution == null || remoteMajor == null) {
                    auth.deleteFirebaseUser(email, password)
                    return@runBlocking false
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
                    launch {
                        userDao.saveUser(remoteUser)
                    }
                    userCache = remoteUser
                    auth.user = remoteUser

                    return@runBlocking true
                } else {
                    auth.deleteFirebaseUser(email, password)
                    return@runBlocking false
                }
            } else {
                return@runBlocking false
            }
        }
    }

    override fun resetPassword(email: String): Boolean {
        return runBlocking {
            return@runBlocking auth.resetPassword(email)
        }
    }

    override fun signOut(): Boolean {
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
    override fun deleteAccount(email: String, password: String): Boolean {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        // TODO: Delete all local data
        return runBlocking {
            getSignedInUser().collect() {
                userService.removeUser(it.userID)
            }
            return@runBlocking auth.deleteFirebaseUser(email, password)
        }
    }

    override fun getMajors(prefix: String): List<String> {
        return runBlocking {
            return@runBlocking userService.getMajors(prefix)?.map {
                it.name
            } ?: emptyList()
        }
    }

    override fun getMajor(name: String): Major? {
        return runBlocking {
            val majors = userService.getMajors(name)
            if (majors?.map {
                    it.name
                }?.contains(name) != true) {
                return@runBlocking userService.newMajor(Major(-1, name))
            } else {
                return@runBlocking majors.last {
                    it.name == name
                }
            }
        }
    }

    override fun getColleges(prefix: String): List<String> {
        return runBlocking {
            return@runBlocking userService.getColleges(prefix)?.map {
                it.name
            } ?: emptyList()
        }
    }

    override fun getCollege(name: String): Institution? {
        return runBlocking {
            val colleges = userService.getColleges(name)
            if (colleges?.map {
                    it.name
                }?.contains(name) != true) {
                return@runBlocking userService.newCollege(Institution(-1, name))
            } else {
                return@runBlocking colleges.last {
                    it.name == name
                }
            }
        }
    }

    companion object : SingletonHolder<UserRepository, Context>({ UserRepository(it) })
}