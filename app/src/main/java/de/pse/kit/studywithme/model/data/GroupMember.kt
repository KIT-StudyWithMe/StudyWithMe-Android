package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["group_ID", "user_ID"])
data class GroupMember(
    @ColumnInfo(name = "group_ID")
    val groupID: Int,

    @ColumnInfo(name = "user_ID")
    val userID : Int,

    @ColumnInfo(name = "is_admin")
    val isAdmin: Boolean,

    @ColumnInfo(name = "is-member")
    val isMember: Boolean
)