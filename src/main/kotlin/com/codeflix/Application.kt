package com.codeflix

import com.codeflix.plugins.configureRouting
import com.codeflix.plugins.configureSerialization
import com.codeflix.repository.DatabaseFactory
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val db = DatabaseFactory()
    configureSerialization()
    configureRouting(db = db)
}