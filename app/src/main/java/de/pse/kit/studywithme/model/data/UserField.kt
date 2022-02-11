package de.pse.kit.studywithme.model.data

import kotlinx.serialization.Serializable

/**
 * Enum class for identification of reports
 *
 * @constructor Create empty User field
 */
@Serializable
enum class UserField {
    NAME,
    INSTITUTION,
    MAJOR,
    CONTACT
}