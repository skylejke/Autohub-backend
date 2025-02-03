package ru.point.feature.login.routing

import cache.InMemoryCache
import cache.TokenCache
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.point.feature.login.model.LoginRequest
import ru.point.feature.login.model.LoginResponse
import ru.point.utils.JwtTokenUtils

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val request = call.receive<LoginRequest>()
            val user = InMemoryCache.userList.firstOrNull { it.username == request.username }
            when {
                user == null -> call.respond(HttpStatusCode.Forbidden)

                user.password != request.password -> call.respond(HttpStatusCode.Forbidden, "Password is incorrect")

                user.password == request.password -> {
                    val token = JwtTokenUtils.generate(request.username)
                    InMemoryCache.tokenList.add(TokenCache(username = request.username, token = token))
                    call.respond(LoginResponse(token = token))
                }

                else -> call.respond(HttpStatusCode.BadRequest, "Something went wrong")
            }
        }
    }
}