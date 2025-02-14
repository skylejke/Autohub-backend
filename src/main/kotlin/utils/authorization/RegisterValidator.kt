package ru.point.utils.authorization

import io.ktor.http.*
import ru.point.database.users.UsersTable
import ru.point.feature.authorization.register.model.RegisterRequest
import ru.point.utils.ValidationError

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

fun String.isValidUserName(): Boolean {
    return this.isNotBlank() && this.length >= 5 && this.length <= 20 && !this.first().isDigit()
}

fun String.isValidPassword(): Boolean {
    return this.isNotBlank() && this.length >= 8
}

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$").matches(this)
}

fun String.isValidPhoneNumber(): Boolean {
    return this.isNotBlank() && Regex("^(\\+7|8)[0-9]{10}$").matches(this)
}