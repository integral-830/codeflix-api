package com.codeflix.plugins

import com.codeflix.data.model.Folder
import com.codeflix.data.model.SimpleResponse
import com.codeflix.repository.DatabaseFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.folderRoutes(db: DatabaseFactory) {

    route("/folder") {

        post {
            val folder = try {
                call.receive<Folder>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, e.message ?: "Missing Fields"))
                return@post
            }
            try {
                db.addFolder(folder = folder)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Folder created successfully"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Folder could not be created"))
            }
        }

        get {
            val id = try {
                call.request.queryParameters["id"]!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, e.message ?: "Course id not found "))
                return@get
            }
            try {
                val folders = db.getAllFolders(courseId = id)
                call.respond(HttpStatusCode.OK, folders)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Folders not found"))
            }
        }

        patch {
            val folder = try {
                call.receive<Folder>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
                return@patch
            }

            try {
                val result = db.updateFolder(folder = folder)
                if (result)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Folder updated successfully"))
                else
                    call.respond(HttpStatusCode.OK, SimpleResponse(false, "Folder not found"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Folder could not be updated"))
            }
        }

        delete {
            val id = try {
                call.request.queryParameters["id"]!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, e.message ?: "Folder id not found "))
                return@delete
            }
            try {
                val result = db.deleteFolder(folderId = id)
                if (result)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "Folder deleted successfully"))
                else
                    call.respond(HttpStatusCode.Conflict, SimpleResponse(false, "Folder not found"))

            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Folder could not be deleted"))
            }

        }

    }

}