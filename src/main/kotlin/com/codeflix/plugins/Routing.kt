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

    val passKey = System.getenv("PASS_KEY")

    routing {
        get("/") {
            call.respondText("CodeFlix Api is Running!")
        }
        courseRoutes(db = db, key = passKey)
        folderRoutes(db = db, key = passKey)
        videoRoutes(db = db, key = passKey)

        androidCourseRoutes(db = db, key = passKey)
        androidFolderRoutes(db= db, key = passKey)
        androidVideoRoutes(db = db, key = passKey)

        webCourseRoutes(db = db, key = passKey)
        webFolderRoutes(db = db, key = passKey)
        webVideoRoutes(db = db, key = passKey)
    }

}
