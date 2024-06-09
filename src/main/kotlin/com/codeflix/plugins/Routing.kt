package com.codeflix.plugins

import com.codeflix.plugins.generalRoutes.courseRoutes
import com.codeflix.plugins.generalRoutes.folderRoutes
import com.codeflix.plugins.generalRoutes.videoRoutes
import com.codeflix.plugins.seacrhRoute.searchRoutes
import com.codeflix.repository.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(db: DatabaseFactory) {

    val passKey = System.getenv("PASS_KEY")

    routing {
        get("/") {
            call.respondText("CodeFlix Api is Running!")
        }
        courseRoutes(db = db, key = passKey)
        folderRoutes(db = db, key = passKey)
        videoRoutes(db = db, key = passKey)
        searchRoutes(db = db, key = passKey)
    }

}
