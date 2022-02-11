package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class for return value of group by the server
 *
 * @property groupID
 * @property name
 * @property lectureID
 * @property description
 * @property sessionFrequency
 * @property sessionType
 * @property lectureChapter
 * @property exercise
 * @constructor Create empty Remote group
 */
@Serializable
@Entity
data class RemoteGroup(
    @PrimaryKey
    @SerialName(value = "groupID")
    @ColumnInfo(name = "group_ID")
    val groupID: Int,

    @SerialName(value = "name")
    @ColumnInfo(name = "name")
    val name: String,

    @SerialName(value = "lectureID")
    @ColumnInfo(name = "lecture_ID")
    val lectureID: Int,

    @SerialName(value = "description")
    @ColumnInfo(name = "description")
    val description: String,

    @SerialName(value = "sessionFrequency")
    @ColumnInfo(name = "session_frequency")
    val sessionFrequency: SessionFrequency,

    @SerialName(value = "sessionType")
    @ColumnInfo(name = "session_type")
    val sessionType: SessionType,

    @SerialName(value = "lectureChapter")
    @ColumnInfo(name = "lecture_chapter")
    val lectureChapter: Int,

    @SerialName(value = "exercise")
    @ColumnInfo(name = "exercise")
    val exercise: Int,

) {
    /**
     * Companion
     *
     * @constructor Create empty Companion
     */
    companion object {
        /**
         * Transforms a RemoteGroup object into a Group object
         *
         * @param remoteGroup
         * @param lecture
         * @param major
         * @return Group object
         */
        fun toGroup(remoteGroup: RemoteGroup, lecture: Lecture? = null, major: Major? = null): Group {
            return Group(
                groupID =  remoteGroup.groupID,
                name = remoteGroup.name,
                lectureID =  remoteGroup.lectureID,
                lecture = lecture,
                major = major,
                description = remoteGroup.description,
                sessionFrequency = remoteGroup.sessionFrequency,
                sessionType = remoteGroup.sessionType,
                lectureChapter = remoteGroup.lectureChapter,
                exercise = remoteGroup.exercise,
            )
        }

        /**
         * Transforms a Group object into a RemoteGroup object
         *
         * @param group
         * @return RemoteGroup object
         */
        fun toRemoteGroup(group: Group): RemoteGroup {
            return RemoteGroup(
                groupID =  group.groupID,
                name = group.name,
                lectureID =  group.lectureID,
                description = group.description,
                sessionFrequency = group.sessionFrequency,
                sessionType = group.sessionType,
                lectureChapter = group.lectureChapter,
                exercise = group.exercise,
            )
        }
    }
}

