package feature.authorization.register.service

import database.tokens.TokensTable
import database.tokens.model.TokenDto
import database.users.UsersTable
import database.users.model.asRegisterRequestDto
import feature.authorization.common.Token
import feature.authorization.register.model.RegisterRequest
import feature.authorization.register.model.RegisterResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import utils.ValidationException
import utils.authorization.TokenFactory
import utils.authorization.validate
import utils.common.checkDataAvailability
import java.util.*

object RegisterService {
    suspend fun register(call: ApplicationCall) {
        val request = call.receive<RegisterRequest>()
        try {

            request.validate()

            checkDataAvailability(
                username = request.username,
                email = request.email,
                phoneNumber = request.phoneNumber
            )

            val userId = UUID.randomUUID().toString()
            val token = TokenFactory.generate(userId = userId)
            UsersTable.insert(request.asRegisterRequestDto(userId = userId))
            TokensTable.insert(TokenDto(token = token))
            call.respond(HttpStatusCode.Created, RegisterResponse(token = Token(token.token)))
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.Conflict, RegisterResponse(message = e.localizedMessage))
        } catch (e: ValidationException) {
            call.respond(HttpStatusCode.BadRequest, RegisterResponse(message = e.message ?: ""))
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                RegisterResponse(message = "Can't create user: ${e.localizedMessage}")
            )
        }
    }
}
