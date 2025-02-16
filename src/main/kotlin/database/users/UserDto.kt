package ru.point.database.users

import ru.point.feature.authorization.register.model.RegisterRequest

data class UserDto(
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String
)

val RegisterRequest.asUserDto
    get() = UserDto(
        username = username,
        email = email,
        phoneNumber = phoneNumber,
        password = password
    )