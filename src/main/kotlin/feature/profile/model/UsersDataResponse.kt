package feature.profile.model

import database.users.UsersDataResponseDto
import kotlinx.serialization.Serializable

@Serializable
data class UsersDataResponse(
    val username: String,
    val email: String,
    val phoneNumber: String,
)

val UsersDataResponseDto.asUsersDataResponse
    get() = UsersDataResponse(
        username = username,
        email = email,
        phoneNumber = phoneNumber,
    )
