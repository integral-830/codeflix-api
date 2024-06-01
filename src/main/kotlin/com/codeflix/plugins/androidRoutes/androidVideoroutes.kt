package com.codeflix.plugins.androidRoutes

import com.codeflix.data.model.SimpleResponse
import com.codeflix.data.model.Video
import com.codeflix.repository.DatabaseFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.androidVideoRoutes(db: DatabaseFactory) {
    route("/android/video") {

        post {
            val video = try {
                call.receive<Video>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
                return@post
            }
            try {
                db.addAndroidVideo(video = video)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Video created successfully"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Video could not be created"))
            }
        }

        get {
            val id = try {
                call.request.queryParameters["id"]!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, e.message ?: "Folder id not found "))
                return@get
            }
            try {
                val videos = db.getAndroidVideos(folderId = id)
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
            try {
                val result = db.updateAndroidVideo(video = video)
                if (result)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "Video updated successfully"))
                else
                    call.respond(HttpStatusCode.Conflict, SimpleResponse(false, "Video not found"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Video could not be updated"))
            }
        }

        delete {
            val id = try {
                call.request.queryParameters["id"]!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, e.message ?: "Video id not found "))
                return@delete
            }
            try {
                val result = db.deleteAndroidVideo(videoId = id)
                if (result)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "Video deleted successfully"))
                else
                    call.respond(HttpStatusCode.OK, SimpleResponse(false, "Video not found"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Video could not be deleted"))
            }

        }

    }

}