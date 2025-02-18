package feature.authorization.login.controller

import io.ktor.server.application.*
import io.ktor.server.routing.*
import feature.authorization.login.service.LoginService

fun Application.configureLoginController() {
    routing {
        post("/login") {
            LoginService.login(call)
        }
    }
}