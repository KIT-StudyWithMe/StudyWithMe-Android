package de.pse.kit.studywithme.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Enum class for identification of reports
 *
 * @constructor Create empty Group field
 */
@Serializable
@SerialName(value = "StudyGroupField")
enum class GroupField {
    NAME,
    LECTURE,
    DESCRIPTION
}