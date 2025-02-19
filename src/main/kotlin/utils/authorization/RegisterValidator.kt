package utils.authorization

import database.users.UsersTable
import feature.authorization.register.model.RegisterRequest
import utils.ValidationException

fun RegisterRequest.validate() {
    val message = when {
        !this.username.isValidUserName() -> "Username is invalid"
        !this.password.isValidPassword() -> "Password is invalid"
        !this.email.isValidEmail() -> "Email is invalid"
        !this.phoneNumber.isValidPhoneNumber() -> "Phone number is invalid"

        UsersTable.getAllUsers().map { it.username }
            .contains(this.username) -> "This username is already taken"

        UsersTable.getAllUsers().map { it.email }
            .contains(this.email) -> "This email is already taken"

        UsersTable.getAllUsers().map { it.phoneNumber }
            .contains(this.phoneNumber) -> "This phone number is already taken"

        else -> null
    }
    message?.let { throw ValidationException(it) }
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