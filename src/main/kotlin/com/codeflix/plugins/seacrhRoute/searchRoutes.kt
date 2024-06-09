package com.codeflix.plugins.seacrhRoute

import com.codeflix.data.model.SimpleResponse
import com.codeflix.repository.DatabaseFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.searchRoutes(db: DatabaseFactory, key: String) {
    route("/search") {
        get {
            val query = try {
                call.request.queryParameters["query"]!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, e.message ?: "Query is missing"))
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
                val courses = db.getCourses(query = query, page = pageInt, limit = limitInt)
                call.respond(HttpStatusCode.OK, courses)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Course not found"))
            }
        }
    }
}