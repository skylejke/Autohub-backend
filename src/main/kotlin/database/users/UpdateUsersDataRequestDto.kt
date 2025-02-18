package database.users

import feature.profile.model.UpdateUsersDataRequest

data class UpdateUsersDataRequestDto(
    val username: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val password: String? = null
)

val UpdateUsersDataRequest.asUpdateUsersDataRequestDto
    get() = UpdateUsersDataRequestDto(
        username = username,
        email = email,
        phoneNumber = phoneNumber,
        password = password
    )