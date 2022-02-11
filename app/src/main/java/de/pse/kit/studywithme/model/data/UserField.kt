package de.pse.kit.studywithme.model.data

import kotlinx.serialization.Serializable

@Serializable
enum class UserField {
    NAME,
    INSTITUTION,
    MAJOR,
    CONTACT
}