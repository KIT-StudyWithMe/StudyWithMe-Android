package de.pse.kit.studywithme.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(value = "StudyGroupField")
enum class GroupField {
    NAME,
    LECTURE,
    DESCRIPTION
}