package com.codeflix.plugins.generalRoutes

import com.codeflix.data.model.SimpleResponse
import com.codeflix.data.model.Video
import com.codeflix.repository.DatabaseFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.videoRoutes(db: DatabaseFactory, key: String) {
    route("/video") {

        post {
            val video = try {
                call.receive<Video>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
                return@post
            }
            val passKey = call.request.header("Pass_Key")
            if (passKey == key) {
                try {
                    db.addVideo(video = video)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "Video created successfully"))
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        SimpleResponse(false, e.message ?: "Video could not be created")
                    )
                }
            } else {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, "PassKey not found"))
            }
        }

        get {
            val page = try {
                call.request.queryParameters["page"]!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, e.message ?: "Page is missing"))
                return@get
            }
            val limit = try {
                call.request.queryParameters["limit"]!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, e.message ?: "Limit is missing"))
                return@get
            }
            val pageInt = try {
                page.toInt()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    SimpleResponse(false, "Page value is not accepted")
                )
                return@get
            }
            val limitInt = try {
                limit.toInt()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    SimpleResponse(false, "Limit value is not accepted")
                )
                return@get
            }
            val id = try {
                call.request.queryParameters["id"]!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, e.message ?: "Folder id not found "))
                return@get
            }
            try {
                val videos = db.getVideos(folderId = id, page = pageInt, limit = limitInt)
                call.respond(HttpStatusCode.OK, videos)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Videos not found"))
            }
        }

        patch {
            val video = try {
                call.receive<Video>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
                return@patch
            }
            val passKey = call.request.header("Pass_Key")
            if (passKey == key) {
                try {
                    val result = db.updateVideo(video = video)
                    if (result)
                        call.respond(HttpStatusCode.OK, SimpleResponse(true, "Video updated successfully"))
                    else
                        call.respond(HttpStatusCode.Conflict, SimpleResponse(false, "Video not found"))
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        SimpleResponse(false, e.message ?: "Video could not be updated")
                    )
                }
            } else {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, "PassKey not found"))
            }
        }

        delete {
            val id = try {
                call.request.queryParameters["id"]!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, e.message ?: "Video id not found "))
                return@delete
            }
            val passKey = call.request.header("Pass_Key")
            if (passKey == key) {
                try {
                    val result = db.deleteVideo(videoId = id)
                    if (result)
                        call.respond(HttpStatusCode.OK, SimpleResponse(true, "Video deleted successfully"))
                    else
                        call.respond(HttpStatusCode.OK, SimpleResponse(false, "Video not found"))
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        SimpleResponse(false, e.message ?: "Video could not be deleted")
                    )
                }
            } else {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, "PassKey not found"))
            }
        }
    }
}