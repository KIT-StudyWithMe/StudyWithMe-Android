package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(primaryKeys = ["group_ID", "user_ID"])
data class GroupMember(
    @SerialName(value = "groupID")
    @ColumnInfo(name = "group_ID")
    val groupID: Int,

    @SerialName(value = "userID")
    @ColumnInfo(name = "user_ID")
    val userID: Int,

    @SerialName(value = "name")
    @ColumnInfo(name = "name")
    val name: String,

    @SerialName(value = "isAdmin")
    @ColumnInfo(name = "is_admin")
    val isAdmin: Boolean,
)
