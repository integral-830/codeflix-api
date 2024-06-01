package com.codeflix.plugins.androidRoutes

import com.codeflix.data.model.Course
import com.codeflix.data.model.SimpleResponse
import com.codeflix.repository.DatabaseFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.androidCourseRoutes(db: DatabaseFactory) {
    route("/android/course") {
        post {
            val course = try {
                call.receive<Course>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
                return@post
            }
            try {
                db.addAndroidCourse(course = course)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Course created successfully"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Course could not be created"))
            }
        }

        get {
            try {
                val courses = db.getAndroidCourses()
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

            try {
                val result = db.updateAndroidCourse(course = course)
                if (result)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "Course updated successfully"))
                else
                    call.respond(HttpStatusCode.OK, SimpleResponse(false, "Course not found"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Course could not be updated."))
            }
        }

        delete {
            val id = try {
                call.request.queryParameters["id"]!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, e.message ?: "Course id not found "))
                return@delete
            }
            try {
                val result = db.deleteAndroidCourse(courseId = id)
                if (result)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "Course deleted successfully"))
                else
                    call.respond(HttpStatusCode.Conflict, SimpleResponse(false, "Course not found"))

            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Course could not be deleted"))
            }

        }

    }

}