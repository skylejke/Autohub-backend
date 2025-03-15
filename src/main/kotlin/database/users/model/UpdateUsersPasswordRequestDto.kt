package database.users.model

import feature.profile.model.UpdateUsersPasswordRequest

@JvmInline
value class UpdateUsersPasswordRequestDto(val password: String)

val UpdateUsersPasswordRequest.asUpdateUsersPasswordRequestDto
    get() = UpdateUsersPasswordRequestDto(password = password)