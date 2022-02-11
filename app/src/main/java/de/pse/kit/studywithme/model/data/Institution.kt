package de.pse.kit.studywithme.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class for the object institution
 *
 * @property institutionID
 * @property name
 * @constructor Create empty Institution
 */
@Serializable
data class Institution (
    @SerialName(value = "institutionID")
    val institutionID: Long,

    @SerialName(value = "name")
    val name: String
)
