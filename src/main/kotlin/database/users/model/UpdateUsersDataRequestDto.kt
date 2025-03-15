package database.users.model

import feature.profile.model.UpdateUsersDataRequest

data class UpdateUsersDataRequestDto(
    val username: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
)

val UpdateUsersDataRequest.asUpdateUsersDataRequestDto
    get() = UpdateUsersDataRequestDto(
        username = username,
        email = email,
        phoneNumber = phoneNumber,
    )