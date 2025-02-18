package feature.authorization.login.service

import database.users.UsersTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.point.database.tokens.TokenDto
import ru.point.database.tokens.TokensTable
import ru.point.feature.authorization.login.model.LoginRequest
import ru.point.feature.authorization.login.model.LoginResponse
import ru.point.utils.authorization.TokenFactory
import utils.authorization.UserNotFoundException

object LoginService {
    suspend fun login(call: ApplicationCall) {
        val request = call.receive<LoginRequest>()

        try {
            val userDto = UsersTable.getUserByUsername(request.username)
            when {
                userDto.password != request.password -> call.respond(HttpStatusCode.Forbidden, "Password is incorrect")

                userDto.password == request.password -> {
                    val token = TokenFactory.generate(request.username)
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