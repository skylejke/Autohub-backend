package feature.authorization.register.controller

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.point.feature.authorization.register.controller.RegisterService

fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            RegisterService.register(call)
        }
    }
}

