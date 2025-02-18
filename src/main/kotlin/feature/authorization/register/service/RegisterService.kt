package ru.point.feature.authorization.register.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.point.database.tokens.TokenDto
import ru.point.database.tokens.TokensTable
import database.users.UsersTable
import database.users.asRegisterRequestDto
import ru.point.feature.authorization.register.model.RegisterRequest
import feature.authorization.register.model.RegisterResponse
import utils.ValidationException
import ru.point.utils.authorization.TokenFactory
import utils.authorization.validate

object RegisterService {
    suspend fun register(call: ApplicationCall) {
        val request = call.receive<RegisterRequest>()

        try {
            request.validate()
            UsersTable.getUserByUsername(request.username)
        } catch (e: ValidationException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "")
        }


        if (UsersTable.getUserByUsername(request.username) != null) {
            TODO("ПОФИКСИТЬ ЭТО")
            call.respond(HttpStatusCode.Conflict, "User already exists")
        }

        val token = TokenFactory.generate(request.username)

        try {
            UsersTable.insert(request.asRegisterRequestDto)
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "User already exists: ${e.localizedMessage}")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Can't create user: ${e.localizedMessage}")
        }

        TokensTable.insert(TokenDto(token = token))

        call.respond(HttpStatusCode.Created, RegisterResponse(token = token))
    }
}