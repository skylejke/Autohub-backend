package ru.point.app

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.jetbrains.exposed.sql.Database
import feature.authorization.login.controller.configureLoginController
import feature.authorization.register.controller.configureRegisterRouting
import feature.profile.controller.configureProfileController
import ru.point.feature.cars.routing.configureCarsController

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabase()
    configureLoginController()
    configureRegisterRouting()
    configureCarsController()
    configureProfileController()
}

fun configureDatabase() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/autohub",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "1234"
    )
}

fun Application.configureSerialization() {
    install(ContentNegotiation){
        json()
    }
}