package utils.authorization

import database.users.UsersTable
import feature.authorization.register.model.RegisterRequest
import utils.ValidationException
import utils.common.isValidEmail
import utils.common.isValidPassword
import utils.common.isValidPhoneNumber
import utils.common.isValidUserName

fun RegisterRequest.validate() {
    val users = UsersTable.getAllUsers()
    val message = when {
        !this.username.isValidUserName() -> "Username is invalid"
        !this.password.isValidPassword() -> "Password is invalid"
        !this.email.isValidEmail() -> "Email is invalid"
        !this.phoneNumber.isValidPhoneNumber() -> "Phone number is invalid"

        users.map { it.username }
            .contains(this.username) -> "This username is already taken"

        users.map { it.email }
            .contains(this.email) -> "This email is already taken"

        users.map { it.phoneNumber }
            .contains(this.phoneNumber) -> "This phone number is already taken"

        else -> null
    }
    message?.let { throw ValidationException(it) }
}

