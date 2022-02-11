package de.pse.kit.studywithme.model.data

/**
 * Data class for the object group
 *
 * @property groupID
 * @property name
 * @property lectureID
 * @property lecture
 * @property major
 * @property description
 * @property sessionFrequency
 * @property sessionType
 * @property lectureChapter
 * @property exercise
 * @constructor Create empty Group
 */
data class Group (
    val groupID: Int,

    val name: String,

    var lectureID: Int,

    val lecture: Lecture? = null,

    val major: Major? = null,

    val description: String,

    val sessionFrequency: SessionFrequency,

    val sessionType: SessionType,

    val lectureChapter: Int,

    val exercise: Int,

)