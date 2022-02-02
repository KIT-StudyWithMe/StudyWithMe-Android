package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Lecture
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface LectureService {
    suspend fun getLectures(prefix: String): List<Lecture>

    companion object{
        val instance: LectureServiceImpl? by lazy {
            LectureServiceImpl(client = HttpClient(Android) {
                install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
            })
        }
    }
}