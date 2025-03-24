package feature.profile.service

import database.users.UsersTable
import database.users.model.asUpdateUsersDataRequestDto
import database.users.model.asUpdateUsersPasswordRequestDto
import feature.profile.model.request.UpdateUsersDataRequest
import feature.profile.model.response.UpdateUserPasswordResponse
import feature.profile.model.request.UpdateUsersPasswordRequest
import feature.profile.model.response.UpdateUserDataResponse
import feature.profile.model.response.asUsersDataResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import utils.ValidationException
import utils.authorization.NoUserIdException
import utils.authorization.UserNotFoundException
import utils.profile.validate

object ProfileService {
    suspend fun getUsersData(call: ApplicationCall) {
        try {
            val usersDataResponseDto = UsersTable.getUserById(getUsersId(call))
            call.respond(HttpStatusCode.OK, usersDataResponseDto.asUsersDataResponse)
        } catch (e: NoUserIdException) {
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
        } catch (e: UserNotFoundException) {
            call.respond(HttpStatusCode.NotFound, "User not found: ${e.message}")
        }
    }

    suspend fun updateUsersData(call: ApplicationCall) {
        try {
            val request = call.receive<UpdateUsersDataRequest>()
            request.validate()
            UsersTable.updateUsersData(getUsersId(call), request.asUpdateUsersDataRequestDto)
            call.respond(HttpStatusCode.OK, UpdateUserDataResponse("Successfully updated users data"))
        } catch (e: ValidationException) {
            call.respond(HttpStatusCode.BadRequest, UpdateUserDataResponse(e.message ?: ""))
        } catch (e: NoUserIdException) {
            call.respond(HttpStatusCode.BadRequest, UpdateUserDataResponse(e.localizedMessage))
        } catch (e: UserNotFoundException) {
            call.respond(HttpStatusCode.NotFound, UpdateUserDataResponse("User not found: ${e.message}"))
        }
    }

    suspend fun updateUserPassword(call: ApplicationCall) {
        try {
            val request = call.receive<UpdateUsersPasswordRequest>()
            request.validate()
            val user = UsersTable.getUserById(getUsersId(call))
            if (user.password != request.oldPassword) {
                call.respond(HttpStatusCode.Forbidden, UpdateUserPasswordResponse("Old password is incorrect"))
                return
            }
            UsersTable.updateUsersPassword(getUsersId(call), request.asUpdateUsersPasswordRequestDto)
            call.respond(HttpStatusCode.OK, UpdateUserPasswordResponse("Successfully updated users password"))
        } catch (e: ValidationException) {
            call.respond(HttpStatusCode.BadRequest, UpdateUserPasswordResponse(e.message ?: ""))
        } catch (e: NoUserIdException) {
            call.respond(HttpStatusCode.BadRequest, UpdateUserPasswordResponse(e.localizedMessage))
        } catch (e: UserNotFoundException) {
            call.respond(HttpStatusCode.NotFound, UpdateUserPasswordResponse("User not found: ${e.message}"))
        }
    }

    suspend fun deleteUser(call: ApplicationCall) {
        try {
            UsersTable.deleteUserById(getUsersId(call))
            call.respond(HttpStatusCode.OK, "Successfully deleted user")
        } catch (e: NoUserIdException) {
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
        } catch (e: UserNotFoundException) {
            call.respond(HttpStatusCode.NotFound, "User not found: ${e.message}")
        }
    }

    private fun getUsersId(call: ApplicationCall) = call.parameters["userId"]?.lowercase()
        ?: throw NoUserIdException()
}