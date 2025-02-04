package ru.point.feature.authorization.login.routing

import cache.InMemoryCache
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.point.feature.authorization.login.model.LoginRequest
import ru.point.feature.authorization.login.model.LoginResponse
import ru.point.utils.TokenFactory

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val request = call.receive<LoginRequest>()
            val user = InMemoryCache.userList.firstOrNull { it.username == request.username }
            when {
                user == null -> call.respond(HttpStatusCode.Forbidden)

                user.password != request.password -> call.respond(HttpStatusCode.Forbidden, "Password is incorrect")

                user.password == request.password -> {
                    val token = TokenFactory.generate(request.username)
                    InMemoryCache.tokenList.add(token)
                    call.respond(LoginResponse(token = token))
                }

                else -> call.respond(HttpStatusCode.BadRequest, "Something went wrong")
            }
        }
    }
}