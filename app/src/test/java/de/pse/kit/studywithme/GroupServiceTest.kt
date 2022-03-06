package de.pse.kit.studywithme

import de.pse.kit.studywithme.model.data.GroupMember
import de.pse.kit.studywithme.model.data.RemoteGroup
import de.pse.kit.studywithme.model.data.SessionFrequency
import de.pse.kit.studywithme.model.data.SessionType
import de.pse.kit.studywithme.model.network.GroupService.Companion.client
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GroupServiceTest {
    private lateinit var mockGroupData: MutableMap<Int, RemoteGroup>
    private val mockGroups: List<RemoteGroup>
        get() = mockGroupData.toList().map { it.second }
    private lateinit var mockGroupMembers: MutableMap<Int, List<GroupMember>>

    @Before
    fun initGroupMockData() {
        mockGroupData = mutableMapOf(
            0 to RemoteGroup(
                groupID = 0,
                name = "test",
                lectureID = 1,
                description = "test",
                sessionFrequency = SessionFrequency.ONCE,
                sessionType = SessionType.HYBRID,
                lectureChapter = 1,
                exercise = 1
            )
        )

        mockGroupMembers = mutableMapOf(
            0 to listOf(
                GroupMember(groupID = 0, userID = 0, name = "test", isAdmin = true),
                GroupMember(groupID = 0, userID = 2, name = "test2", isAdmin = false)
            )
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getGroupsTest() = runBlocking {
        val mockEngine = successMockEngine(Json.encodeToString(mockGroups))
        val apiClient = client(mockEngine)

        val apiGroups = apiClient.getGroups("test")

        println(apiGroups)
        Assert.assertEquals(apiGroups, mockGroups)
    }

    @Test
    fun getJoinedGroupsTest() = runBlocking {
        val mockEngine = successMockEngine(Json.encodeToString(mockGroups))
        val apiClient = client(mockEngine)

        val apiGroups = apiClient.getJoinedGroups(0)

        println(apiGroups)
        Assert.assertEquals(apiGroups, mockGroups)
    }

    @Test
    fun getGroupSuggestionsTest() = runBlocking {
        val mockEngine = successMockEngine(Json.encodeToString(mockGroups))
        val apiClient = client(mockEngine)

        val apiGroup = apiClient.getGroupSuggestions(0)

        println(apiGroup)
        Assert.assertEquals(apiGroup, mockGroups)
    }

    @Test
    fun getGroupTest() = runBlocking {
        val mockGroup = mockGroupData[0]
        val mockEngine = successMockEngine(Json.encodeToString(mockGroup))
        val apiClient = client(mockEngine)

        val apiGroup = apiClient.getGroup(0)

        println(apiGroup)
        Assert.assertEquals(apiGroup, mockGroup)
    }

    @Test
    fun getGroupWithTooMuchDataTest() = runBlocking {
        val mockGroup = mockGroupData[0]
        val mockEngine = successMockEngine("""{"groupID":0,"name":"test","description":"test","lectureID":1,"sessionFrequency":"ONCE","sessionType":"HYBRID","lectureChapter":1,"exercise":1, "hidden":true}""")
        val apiClient = client(mockEngine)

        val apiGroup = apiClient.getGroup(0)

        println(apiGroup)
        Assert.assertEquals(apiGroup, mockGroup)
    }

    @Test
    fun getGroupMembersTest() = runBlocking {
        val mockEngine = successMockEngine(Json.encodeToString(mockGroupMembers[0]))
        val apiClient = client(mockEngine)

        val apiGroupMembers = apiClient.getGroupMembers(0)

        println(apiGroupMembers)
        Assert.assertEquals(apiGroupMembers, mockGroupMembers[0])
    }

    @Test
    fun newGroupTest() = runBlocking {
        val newGroup = RemoteGroup(
            groupID = 1,
            "newGroup",
            lectureID = 1,
            description = "test",
            sessionFrequency = SessionFrequency.WEEKLY,
            sessionType = SessionType.PRESENCE,
            lectureChapter = 1,
            exercise = 2
        )

        val mockEngine = successMockEngine(Json.encodeToString(newGroup))
        val apiClient = client(mockEngine)

        val apiGroup = apiClient.newGroup(newGroup, 0)

        println(apiGroup)
        Assert.assertEquals(apiGroup, newGroup)
    }

    @Test
    fun editGroup() = runBlocking {
        val editedGroup = RemoteGroup(
            groupID = 0,
            "editedGroup",
            lectureID = 1,
            description = "test",
            sessionFrequency = SessionFrequency.WEEKLY,
            sessionType = SessionType.PRESENCE,
            lectureChapter = 1,
            exercise = 2
        )

        val mockEngine = successMockEngine(Json.encodeToString(editedGroup))
        val apiClient = client(mockEngine)

        val apiGroup = apiClient.editGroup(editedGroup.groupID, editedGroup)

        println(apiGroup)
        Assert.assertEquals(apiGroup, editedGroup)
    }

    @Test
    fun removeGroup() = runBlocking {
        val mockEngine = successMockEngine()
        val apiClient = client(mockEngine)

        val result = apiClient.removeGroup(0)

        println(result)
        Assert.assertEquals(result, true)
    }

    private fun successMockEngine(content: String = "") = MockEngine {
        respond(
            content = content,
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }
}