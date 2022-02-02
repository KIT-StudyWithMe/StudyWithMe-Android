package de.pse.kit.studywithme.model.repository

import android.content.Context
import de.pse.kit.studywithme.SingletonHolder
import de.pse.kit.studywithme.model.data.Lecture
import de.pse.kit.studywithme.model.network.LectureService
import kotlinx.coroutines.runBlocking

class LectureRepository private constructor(context: Context) {
    private val lectureService = LectureService.instance

    fun getLectures(prefix: String): List<Lecture> {
        return runBlocking {
            return@runBlocking lectureService!!.getLectures(prefix)
        }
    }

    fun saveLecture(lecture: Lecture) {
        //TODO
    }

    fun removeLecture(lectureID: Int) {
        //TODO
    }

    companion object : SingletonHolder<LectureRepository, Context>({ LectureRepository(it) })
}