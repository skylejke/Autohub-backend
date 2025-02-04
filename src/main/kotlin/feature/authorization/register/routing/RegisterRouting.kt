package ru.point.feature.authorization.register.routing

import cache.InMemoryCache
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.point.feature.authorization.register.model.RegisterRequest
import ru.point.feature.authorization.register.model.RegisterResponse
import ru.point.utils.TokenFactory
import ru.point.utils.validate

fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            val request = call.receive<RegisterRequest>()

            request.validate()?.let {
                call.respond(it.httpStatusCode, it.message)
            }

            val token = TokenFactory.generate(request.username)
            InMemoryCache.userList.add(request)
            InMemoryCache.tokenList.add(token)

            call.respond(RegisterResponse(token = token))
            return@post
        }
    }
}

