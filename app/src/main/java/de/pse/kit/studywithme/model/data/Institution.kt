package de.pse.kit.studywithme.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Institution (
    @SerialName(value = "institutionID")
    val institutionID: Long,

    @SerialName(value = "name")
    val name: String
)
