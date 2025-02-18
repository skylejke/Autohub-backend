package database.users

import ru.point.feature.authorization.register.model.RegisterRequest

data class RegisterRequestDto(
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String
)

val RegisterRequest.asRegisterRequestDto
    get() = RegisterRequestDto(
        username = username,
        email = email,
        phoneNumber = phoneNumber,
        password = password
    )