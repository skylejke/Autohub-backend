package ru.point

import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import ru.point.feature.authorization.login.routing.configureLoginRouting
import ru.point.feature.authorization.register.routing.configureRegisterRouting

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabase()
    configureRouting()
    configureLoginRouting()
    configureRegisterRouting()
}

fun configureDatabase() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/autohub",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "1234"
    )
}