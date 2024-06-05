package com.codeflix.plugins.generalRoutes

import com.codeflix.data.model.Folder
import com.codeflix.data.model.SimpleResponse
import com.codeflix.repository.DatabaseFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.webFolderRoutes(db: DatabaseFactory, key: String) {

    route("/web/folder") {

        post {
            val folder = try {
                call.receive<Folder>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, e.message ?: "Missing Fields"))
                return@post
            }
            val passKey = call.request.header("Pass_Key")
            if (passKey == key) {
                try {
                    db.addWebFolder(folder = folder)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "Folder created successfully"))
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        SimpleResponse(false, e.message ?: "Folder could not be created")
                    )
                }
            } else {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, "PassKey not found"))
            }
        }

        get {
            val id = try {
                call.request.queryParameters["id"]!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, e.message ?: "Course id not found "))
                return@get
            }
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
            try {
                val folders = db.getWebFolders(courseId = id, page = pageInt, limit = limitInt)
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
            val passKey = call.request.header("Pass_Key")
            if (passKey == key) {
                try {
                    val result = db.updateWebFolder(folder = folder)
                    if (result)
                        call.respond(HttpStatusCode.OK, SimpleResponse(true, "Folder updated successfully"))
                    else
                        call.respond(HttpStatusCode.OK, SimpleResponse(false, "Folder not found"))
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        SimpleResponse(false, e.message ?: "Folder could not be updated")
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
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, e.message ?: "Folder id not found "))
                return@delete
            }
            val passKey = call.request.header("Pass_Key")
            if (passKey == key) {
                try {
                    val result = db.deleteWebFolder(folderId = id)
                    if (result)
                        call.respond(HttpStatusCode.OK, SimpleResponse(true, "Folder deleted successfully"))
                    else
                        call.respond(HttpStatusCode.Conflict, SimpleResponse(false, "Folder not found"))

                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        SimpleResponse(false, e.message ?: "Folder could not be deleted")
                    )
                }
            } else {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, "PassKey not found"))
            }
        }
    }
}