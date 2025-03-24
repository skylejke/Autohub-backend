package feature.profile.model.response

import database.users.model.UsersDataResponseDto
import kotlinx.serialization.Serializable

@Serializable
data class UsersDataResponse(
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
)

val UsersDataResponseDto.asUsersDataResponse
    get() = UsersDataResponse(
        username = username,
        email = email,
        phoneNumber = phoneNumber,
        password = password
    )
