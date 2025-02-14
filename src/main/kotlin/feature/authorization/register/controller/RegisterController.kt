package ru.point.feature.authorization.register.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.point.database.tokens.TokenDto
import ru.point.database.tokens.TokensTable
import ru.point.database.users.UserDto
import ru.point.database.users.UsersTable
import ru.point.feature.authorization.register.model.RegisterRequest
import ru.point.feature.authorization.register.model.RegisterResponse
import ru.point.utils.authorization.TokenFactory
import ru.point.utils.authorization.validate

class RegisterController(private val call: ApplicationCall) {
    suspend fun register() {
        val request = call.receive<RegisterRequest>()

        request.validate()?.let {
            call.respond(it.httpStatusCode, it.message)
        }

        val userDto = UsersTable.getUserByUsername(request.username)

        if (userDto != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        }

        val token = TokenFactory.generate(request.username)

        try {
            UsersTable.insert(
                UserDto(
                    username = request.username,
                    email = request.email,
                    phoneNumber = request.phoneNumber,
                    password = request.password,
                )
            )
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "User already exists: ${e.localizedMessage}")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Can't create user: ${e.localizedMessage}")
        }

        TokensTable.insert(TokenDto(token = token))

        call.respond(RegisterResponse(token = token))
    }
}