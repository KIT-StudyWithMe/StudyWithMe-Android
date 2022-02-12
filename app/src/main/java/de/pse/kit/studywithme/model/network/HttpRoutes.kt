package de.pse.kit.studywithme.model.network

/**
 * Object for shortcuts of serveradresses
 *
 * @constructor Create empty Http routes
 */
object HttpRoutes {
    const val TAG = "REST CALL"
    private const val BASE_URL = "https://api.prod.studywithme.ovh"
    const val USERS = "$BASE_URL/users/"
    const val GROUPS = "$BASE_URL/groups/"
    const val SESSIONS = "$BASE_URL/sessions/"
    const val MAJORS = "$BASE_URL/majors/"
    const val LECTURES = "$BASE_URL/lectures/"
    const val INSTITUTIONS = "$BASE_URL/institutions/"
    const val REPORTS = "$BASE_URL/reports/"
}