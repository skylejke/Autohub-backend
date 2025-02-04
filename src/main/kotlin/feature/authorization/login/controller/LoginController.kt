package ru.point.feature.authorization.login.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.point.database.tokens.TokenDto
import ru.point.database.tokens.TokensTable
import ru.point.database.users.UsersTable
import ru.point.feature.authorization.login.model.LoginRequest
import ru.point.feature.authorization.login.model.LoginResponse
import ru.point.utils.TokenFactory

class LoginController(private val call: ApplicationCall) {
    suspend fun login() {
        val request = call.receive<LoginRequest>()

        val userDto = UsersTable.getUserByUsername(request.username)

        when {
            userDto == null -> call.respond(HttpStatusCode.Forbidden)

            userDto.password != request.password -> call.respond(HttpStatusCode.Forbidden, "Password is incorrect")

            userDto.password == request.password -> {
                val token = TokenFactory.generate(request.username)
                TokensTable.insert(TokenDto(token = token))
                call.respond(LoginResponse(token = token))
            }

            else -> call.respond(HttpStatusCode.BadRequest, "Something went wrong")
        }
    }
}