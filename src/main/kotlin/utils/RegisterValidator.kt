package ru.point.utils

import io.ktor.http.*
import ru.point.database.users.UsersTable
import ru.point.feature.authorization.register.model.RegisterRequest

fun RegisterRequest.validate(): ValidationError? =
    when {
        !this.username.isValidUserName() -> ValidationError(HttpStatusCode.Forbidden, "Username is invalid")
        !this.password.isValidPassword() -> ValidationError(HttpStatusCode.Forbidden, "Password is invalid")
        !this.email.isValidEmail() -> ValidationError(HttpStatusCode.Forbidden, "Email is invalid")
        !this.phoneNumber.isValidPhoneNumber() -> ValidationError(
            HttpStatusCode.Forbidden,
            "Phone number is invalid"
        )

        UsersTable.getAllUsers().map { it.username }.contains(this.username) -> ValidationError(
            HttpStatusCode.Conflict,
            "This username is already taken"
        )

        UsersTable.getAllUsers().map { it.email }.contains(this.email) -> ValidationError(
            HttpStatusCode.Conflict,
            "This email is already taken"
        )

        UsersTable.getAllUsers().map { it.phoneNumber }.contains(this.phoneNumber) -> ValidationError(
            HttpStatusCode.Conflict,
            "This phone number is already taken"
        )

        else -> null
    }


data class ValidationError(val httpStatusCode: HttpStatusCode, val message: String)