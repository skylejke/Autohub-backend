package feature.authorization.login.service

import database.users.UsersTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import database.tokens.TokenDto
import database.tokens.TokensTable
import feature.authorization.login.model.LoginRequest
import feature.authorization.login.model.LoginResponse
import utils.authorization.TokenFactory
import utils.authorization.UserNotFoundException

object LoginService {
    suspend fun login(call: ApplicationCall) {
        val request = call.receive<LoginRequest>()

        try {
            val userDto = UsersTable.getUserByUsername(request.username)
            when {
                userDto.password != request.password -> call.respond(HttpStatusCode.Forbidden, "Password is incorrect")

                userDto.password == request.password -> {
                    val token = TokenFactory.generate(userId = userDto.id)
                    TokensTable.insert(TokenDto(token = token))
                    call.respond(LoginResponse(token = token))
                }

                else -> call.respond(HttpStatusCode.BadRequest, "Something went wrong")
            }
        } catch (e: UserNotFoundException) {
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
        }
    }
}