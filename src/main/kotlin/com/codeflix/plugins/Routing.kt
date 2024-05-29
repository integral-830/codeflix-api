package com.codeflix.plugins

import com.codeflix.data.model.SimpleResponse
import com.codeflix.data.model.Video
import com.codeflix.repository.DatabaseFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(db: DatabaseFactory) {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        courseRoutes(db = db)
        folderRoutes(db = db)
        videoRoutes(db = db)
    }

}
