package de.pse.kit.studywithme

import de.pse.kit.studywithme.model.data.Institution
import de.pse.kit.studywithme.model.data.Major
import de.pse.kit.studywithme.model.data.User
import de.pse.kit.studywithme.model.network.UserService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class UserServiceTest {
    private val service = UserService.instance
    private val userB = User(
        userID = -1,
        name = "Olaf",
        collegeID = -1,
        college = "KIT",
        majorID = -1,
        major = "Informatik B.Sc.",
        contact = "0152987654321",
        firebaseUID = "1bf734bf73nf73"
    )

    @Test
    fun myTest() {
        runBlocking {
            val majors = service.getMajors(userB.major ?: "")
            println(majors)
            assert(majors != null)

            val majorID: Long

            if (!(majors!!.map {
                    it.name
                }.contains(userB.major)) && userB.major != null) {
                val major = service.newMajor(Major(-1, userB.major!!))
                println(major)
                assert(major != null)

                majorID = major!!.majorID
            } else {
                majorID = majors.filter {
                    it.name == userB.major
                }.last().majorID
            }

            val colleges = service.getColleges(userB.college ?: "")
            println(colleges)
            assert(colleges != null)

            val collegeID: Long

            if (!(colleges!!.map {
                    it.name
                }.contains(userB.college)) && userB.college != null) {
                val college = service.newCollege(Institution(-1, userB.college!!))
                println(college)
                assert(college != null)

                collegeID = college!!.institutionID
            } else {
                collegeID = colleges.filter {
                    it.name == userB.college
                }.last().institutionID
            }



            val user = User(
                userID = -1,
                name = "Olaf",
                collegeID = collegeID.toInt(),
                college = "KIT",
                majorID = majorID.toInt(),
                major = "Informatik B.Sc.",
                contact = "0152987654321",
                firebaseUID = "1bf734bf73nf73"
            )


            val newUser = service.newUser(user)
            println(newUser.toString() + "dd")
            assert(newUser != null)

            val remoteUser = service.getUser(newUser!!.userID)
            println(remoteUser)
            assert(remoteUser != null)

            val userByFUID = service.getUser(newUser.firebaseUID)
            println(userByFUID)
            assert(userByFUID != null)

            val ed_user = User(
                userID = newUser.userID,
                name = "OlafEdited",
                collegeID = newUser.collegeID,
                college = newUser.college,
                majorID = newUser.majorID,
                major = newUser.major,
                contact = "0152987654321",
                firebaseUID = "1bf734bf73nf73"
            )

            val editedUser = service.editUser(ed_user.userID, ed_user)
            println(editedUser)
            assert(editedUser != null)

            val deleted = service.removeUser(editedUser!!.userID)
            assert(deleted)
        }
    }
}