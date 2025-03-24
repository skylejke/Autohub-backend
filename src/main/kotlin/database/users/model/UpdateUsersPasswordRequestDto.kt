package database.users.model

import feature.profile.model.request.UpdateUsersPasswordRequest

@JvmInline
value class UpdateUsersPasswordRequestDto(val password: String)

val UpdateUsersPasswordRequest.asUpdateUsersPasswordRequestDto
    get() = UpdateUsersPasswordRequestDto(password = newPassword)