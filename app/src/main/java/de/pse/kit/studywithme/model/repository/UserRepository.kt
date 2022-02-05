package de.pse.kit.studywithme.model.repository

import android.content.Context
import android.util.Log
import de.pse.kit.studywithme.SingletonHolder
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

    override fun isSignedIn(): Boolean {
        if (auth.firebaseUID == null) {
            return false
        }
        return true
    }

    @ExperimentalCoroutinesApi
    override fun getSignedInUser(): Flow<User> {

        if (auth.firebaseUID == null) {
            return flow {
                emit(
                    User(
                        userID = 0,
                        name = "max.mustermann2",
                        contact = "max.mustermann2@gmail.com",
                        firebaseUID = "0"
                    )
                )
            }
            /*
           // TODO: Explicit exception class
           throw Exception("Authentication Error: No local user signed in.")

             */
        }

        return channelFlow {
            val truthWasSend = AtomicBoolean(false)
            send(userCache)

            launch {
                val remoteUser = userService.getUser(auth.firebaseUID)
                send(remoteUser)
                truthWasSend.set(true)
            }
            launch {
                val localUser = userDao.getUser(auth.firebaseUID)
                if (!truthWasSend.get()) {
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
                val remoteUser = userService.getUser(auth.firebaseUID!!)
                if (remoteUser != null) {
                    Log.d(auth.TAG, "Remote Database User Post:success")
                    userDao.saveUser(remoteUser)
                    userCache = remoteUser

                    return@runBlocking true
                } else {
                    return@runBlocking false
                }
            } else {
                return@runBlocking false
            }
        }
    }

    override fun signUp(email: String, password: String, user: User): Boolean {
        return runBlocking {
            if (auth.signUp(email, password)) {
                val remoteUser = userService.newUser(user)
                if (remoteUser != null) {
                    Log.d(auth.TAG, "Remote Database User Post:success")
                    userDao.saveUser(remoteUser)
                    userCache = remoteUser

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
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return runBlocking {
            return@runBlocking auth.resetPassword(email)
        }
    }

    override fun signOut() {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        //TODO: Maye delete all local data
        auth.signOut()
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
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return runBlocking {
            return@runBlocking userService.getMajors(prefix)
        }
    }

    override fun getColleges(prefix: String): List<String> {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
        }

        return runBlocking {
            return@runBlocking userService.getColleges(prefix)
        }
    }

    companion object : SingletonHolder<UserRepository, Context>({ UserRepository(it) })
}