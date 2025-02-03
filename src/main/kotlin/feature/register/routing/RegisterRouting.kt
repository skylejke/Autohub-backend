package ru.point.feature.register.routing

import cache.InMemoryCache
import cache.TokenCache
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.point.feature.register.model.RegisterRequest
import ru.point.feature.register.model.RegisterResponse
import ru.point.utils.JwtTokenUtils
import ru.point.utils.validate

fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            val request = call.receive<RegisterRequest>()

            request.validate()?.let {
                call.respond(it.httpStatusCode, it.message)
            }

            val token = JwtTokenUtils.generate(request.username)
            InMemoryCache.userList.add(request)
            InMemoryCache.tokenList.add(TokenCache(username = request.username, token = token))

            call.respond(RegisterResponse(token = token))
            return@post
        }
    }
}

