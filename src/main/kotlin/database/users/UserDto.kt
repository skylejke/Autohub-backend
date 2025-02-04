package ru.point.database.users

data class UserDto(
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String
)