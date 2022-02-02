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

class UserRepository private constructor(context: Context) {
    private val userService = UserService.instance
    private val userDao = AppDatabase.getInstance(context).userDao()
    private val auth = Authenticator
    private var userCache: User? = null

    fun isSignedIn(): Boolean {
        if (auth.firebaseUID == null) {
            return false
        }
        return true
    }

    @ExperimentalCoroutinesApi
    fun getSignedInUser(): Flow<User> {
        if (auth.firebaseUID == null) {
            // TODO: Explicit exception class
            throw Exception("Authentication Error: No local user signed in.")
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

    fun editSignedInUser(user: User): Boolean {
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

    fun signIn(email: String, password: String): Boolean {
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

    fun signUp(email: String, password: String, user: User): Boolean {
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

    fun resetPassword(email: String): Boolean {
        return runBlocking {
            return@runBlocking auth.resetPassword(email)
        }
    }

    fun signOut() {
        //TODO: Maye delete all local data
        auth.signOut()
    }

    fun deleteAccount(email: String, password: String): Boolean {
        //TODO: Delete all data and leave all groups
        return runBlocking {
            return@runBlocking auth.deleteFirebaseUser(email, password)
        }
    }

    fun getMajors(prefix: String): List<String> {
        return runBlocking {
            return@runBlocking userService.getMajors(prefix)
        }
    }

    fun getColleges(prefix: String): List<String> {
        return runBlocking {
            return@runBlocking userService.getColleges(prefix)
        }
    }

    companion object: SingletonHolder<UserRepository, Context>({ UserRepository(it) })
}