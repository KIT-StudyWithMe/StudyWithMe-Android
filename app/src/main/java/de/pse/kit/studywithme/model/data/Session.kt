package de.pse.kit.studywithme.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

@Serializable
@Entity
data class Session(
    @PrimaryKey
    @SerialName("sessionId")
    @ColumnInfo(name = "session_ID")
    val sessionID: Int,

    @SerialName("groupId")
    @ColumnInfo(name = "group_ID")
    val groupID: Int,

    @SerialName("location")
    @ColumnInfo(name = "location")
    val location: String,

    @Serializable(with = DateSerializer::class)
    @SerialName("date")
    @ColumnInfo(name = "date")
    val date: Date,

    @SerialName("duration")
    @ColumnInfo(name = "duration")
    val duration: Int?
)

object DateSerializer : KSerializer<Date> {
    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeLong(value.time)
    }

    override fun deserialize(decoder: Decoder): Date {
        return Date(decoder.decodeLong())
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.LONG)
}