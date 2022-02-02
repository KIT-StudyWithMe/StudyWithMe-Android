package de.pse.kit.studywithme

import com.google.common.truth.Truth.assertThat
import de.pse.kit.studywithme.model.data.User
import de.pse.kit.studywithme.model.network.UserService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class UserServiceTest {

    private val service = UserService.instance

    @Test
    fun getUserById(): Unit = runBlocking {
        val user = service.getUser(1)
        assertThat(user).isEqualTo(User(1, "Bla", collegeID = null, majorID = null, "email"))
    }

    @Test
    fun getUsers(): Unit = runBlocking {
        val users = service.getUsers()
        assertThat(users).isNotNull()
    }

    @Test
    fun saveAndEditUser(): Unit = runBlocking {
        val user = User(userID = -1, name = "clientTestUser", collegeID = null, majorID = null, contact = "email")
        var userResponse = service.newUser(user)
        assertThat(userResponse).isNotNull()
        assertThat(userResponse!!.name).matches(user.name)
        assertThat(userResponse.contact).matches(user.contact)

        val userEdited = User(userID = -1, name = "clientTestChanged", collegeID = null, majorID = null, contact = "email")
        val userEditedResponse = service.editUser(userResponse.userID, userEdited)

        assertThat(userEditedResponse).isNotNull()
        assertThat(userEditedResponse!!.name).matches(userEdited.name)

        service.removeUser(userEditedResponse.userID)
        userResponse = service.getUser(userEditedResponse.userID)
        assertThat(userResponse).isNull()
    }

    @Test
    fun saveAndRemoveUser(): Unit = runBlocking {
        val user = User(userID = -1, name = "clientTestUser", collegeID = null, majorID = null, contact = "email")
        var userResponse = service.newUser(user)

        assertThat(userResponse).isNotNull()
        assertThat(userResponse!!.name).matches(user.name)
        assertThat(userResponse.contact).matches(user.contact)

        val response = service.removeUser(userResponse.userID)
        userResponse = service.getUser(userResponse.userID)
        assertThat(userResponse).isNull()
    }
}