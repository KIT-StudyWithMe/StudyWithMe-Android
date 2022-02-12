package de.pse.kit.studywithme.model.network

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

/**
 * Object for serialization process
 *
 * @constructor Create empty Date serializer
 */
object DateSerializer : KSerializer<Date> {
    /**
     * Encode date value into long
     *
     * @param encoder
     * @param value
     */
    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeLong(value.time)
    }

    /**
     * Decode long into date
     *
     * @param decoder
     * @return
     */
    override fun deserialize(decoder: Decoder): Date {
        return Date(decoder.decodeLong())
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.LONG)
}