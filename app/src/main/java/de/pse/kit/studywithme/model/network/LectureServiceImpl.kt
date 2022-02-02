package de.pse.kit.studywithme.model.network

import de.pse.kit.studywithme.model.data.Lecture
import io.ktor.client.*

class LectureServiceImpl(private val client: HttpClient): LectureService {
    override suspend fun getLectures(prefix: String): List<Lecture> {
        TODO("Not yet implemented")
    }
}