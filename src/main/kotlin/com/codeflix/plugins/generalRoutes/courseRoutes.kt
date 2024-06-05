package com.codeflix.plugins.generalRoutes

import com.codeflix.data.model.Course
import com.codeflix.data.model.SimpleResponse
import com.codeflix.repository.DatabaseFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.courseRoutes(db: DatabaseFactory, key: String) {
    route("/general/course") {
        post {
            val course = try {
                call.receive<Course>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
                return@post
            }
            val passKey = call.request.header("Pass_Key")
            if (passKey == key) {
                try {
                    db.addGeneralCourse(course = course)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "Course created successfully"))
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        SimpleResponse(false, e.message ?: "Course could not be created")
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
            try {
                val courses = db.getGeneralCourses(page = pageInt, limit = limitInt)
                call.respond(HttpStatusCode.OK, courses)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Course not found"))
            }
        }

        patch {
            val course = try {
                call.receive<Course>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Course Fields"))
                return@patch
            }
            val passKey = call.request.header("Pass_Key")
            if (passKey == key) {
                try {
                    val result = db.updateGeneralCourse(course = course)
                    if (result)
                        call.respond(HttpStatusCode.OK, SimpleResponse(true, "Course updated successfully"))
                    else
                        call.respond(HttpStatusCode.OK, SimpleResponse(false, "Course not found"))
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        SimpleResponse(false, e.message ?: "Course could not be updated.")
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
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, e.message ?: "Course id not found "))
                return@delete
            }
            val passKey = call.request.header("Pass_Key")
            if (passKey == key) {
                try {
                    val result = db.deleteGeneralCourse(courseId = id)
                    if (result)
                        call.respond(HttpStatusCode.OK, SimpleResponse(true, "Course deleted successfully"))
                    else
                        call.respond(HttpStatusCode.Conflict, SimpleResponse(false, "Course not found"))

                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        SimpleResponse(false, e.message ?: "Course could not be deleted")
                    )
                }
            } else {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, "PassKey not found"))
            }
        }
    }
}