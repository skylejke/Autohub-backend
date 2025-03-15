package feature.authorization.register.service

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import database.tokens.model.TokenDto
import database.tokens.TokensTable
import database.users.UsersTable
import database.users.model.asRegisterRequestDto
import feature.authorization.register.model.RegisterRequest
import feature.authorization.register.model.RegisterResponse
import utils.ValidationException
import utils.authorization.TokenFactory
import utils.authorization.validate
import java.util.UUID

object RegisterService {
    suspend fun register(call: ApplicationCall) {
        val request = call.receive<RegisterRequest>()
        try {
            request.validate()
            val userId = UUID.randomUUID().toString()
            val token = TokenFactory.generate(userId = userId)
            UsersTable.insert(request.asRegisterRequestDto(userId = userId))
            TokensTable.insert(TokenDto(token = token))
            call.respond(HttpStatusCode.Created, RegisterResponse(token = token))
        } catch (e: ValidationException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "")
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "User already exists: ${e.localizedMessage}")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Can't create user: ${e.localizedMessage}")
        }
    }
}