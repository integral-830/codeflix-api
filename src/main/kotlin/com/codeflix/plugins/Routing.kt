package com.codeflix.plugins

import com.codeflix.repository.DatabaseFactory
import io.ktor.server.application.*
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
