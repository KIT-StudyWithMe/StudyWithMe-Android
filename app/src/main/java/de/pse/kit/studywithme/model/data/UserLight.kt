package de.pse.kit.studywithme.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class to user object received by the server
 *
 * @property userID
 * @property name
 * @constructor Create empty User light
 */
@Serializable
data class UserLight(

    @SerialName(value = "userID")
    val userID: Long,

    @SerialName(value = "name")
    val name: String
)
