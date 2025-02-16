package ru.point.feature.authorization.register.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.point.database.tokens.TokenDto
import ru.point.database.tokens.TokensTable
import ru.point.database.users.UsersTable
import ru.point.database.users.asUserDto
import ru.point.feature.authorization.register.model.RegisterRequest
import ru.point.feature.authorization.register.model.RegisterResponse
import ru.point.utils.ValidationException
import ru.point.utils.authorization.TokenFactory
import ru.point.utils.authorization.validate

class RegisterController(private val call: ApplicationCall) {
    suspend fun register() {
        val request = call.receive<RegisterRequest>()

        try {
            request.validate()
        } catch (e: ValidationException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "")
        }


        if (UsersTable.getUserByUsername(request.username) != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        }

        val token = TokenFactory.generate(request.username)

        try {
            UsersTable.insert(request.asUserDto)
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "User already exists: ${e.localizedMessage}")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Can't create user: ${e.localizedMessage}")
        }

        TokensTable.insert(TokenDto(token = token))

        call.respond(HttpStatusCode.Created, RegisterResponse(token = token))
    }
}