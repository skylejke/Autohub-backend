package ru.point.feature.authorization.login.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.point.feature.authorization.login.controller.LoginController

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            LoginController(call).login()
        }
    }
}