package feature.authorization.register.controller

import io.ktor.server.application.*
import io.ktor.server.routing.*
import feature.authorization.register.service.RegisterService

fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            RegisterService.register(call)
        }
    }
}

