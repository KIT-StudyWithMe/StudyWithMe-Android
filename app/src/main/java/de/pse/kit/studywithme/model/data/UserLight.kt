package de.pse.kit.studywithme.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserLight(

    @SerialName(value = "userID")
    val userID: Long,

    @SerialName(value = "name")
    val name: String
)
