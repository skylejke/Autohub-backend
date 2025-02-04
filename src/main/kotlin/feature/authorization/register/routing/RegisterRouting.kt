package ru.point.feature.authorization.register.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.point.feature.authorization.register.controller.RegisterController

fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            val registerController = RegisterController(call)
            registerController.register()
        }

    }
}

