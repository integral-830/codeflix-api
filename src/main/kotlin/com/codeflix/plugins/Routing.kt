package com.codeflix.plugins

import com.codeflix.plugins.androidRoutes.androidCourseRoutes
import com.codeflix.plugins.androidRoutes.androidFolderRoutes
import com.codeflix.plugins.androidRoutes.androidVideoRoutes
import com.codeflix.plugins.generalRoutes.*
import com.codeflix.plugins.webRoutes.webVideoRoutes
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

        androidCourseRoutes(db = db)
        androidFolderRoutes(db= db)
        androidVideoRoutes(db = db)

        webCourseRoutes(db = db)
        webFolderRoutes(db = db)
        webVideoRoutes(db = db)
    }

}
