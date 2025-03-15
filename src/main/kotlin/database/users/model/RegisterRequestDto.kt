package database.users.model

import feature.authorization.register.model.RegisterRequest

data class RegisterRequestDto(
    val id: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String
)

fun RegisterRequest.asRegisterRequestDto(userId: String) = RegisterRequestDto(
    id = userId,
    username = username,
    email = email,
    phoneNumber = phoneNumber,
    password = password
)