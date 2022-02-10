package de.pse.kit.studywithme.model.data

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