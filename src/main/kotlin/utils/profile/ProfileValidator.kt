package utils.profile

import database.users.UsersTable
import feature.profile.model.UpdateUsersDataRequest
import feature.profile.model.UpdateUsersPasswordRequest
import utils.ValidationException
import utils.common.isValidEmail
import utils.common.isValidPassword
import utils.common.isValidPhoneNumber
import utils.common.isValidUserName

fun UpdateUsersDataRequest.validate() {
    val users = UsersTable.getAllUsers()
    val message = when {
        this.username?.isValidUserName() == false -> "Username is invalid"
        this.email?.isValidEmail() == false -> "Email is invalid"
        this.phoneNumber?.isValidPhoneNumber() == false -> "Phone number is invalid"

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

fun UpdateUsersPasswordRequest.validate() {
    val message = when {
        !this.password.isValidPassword() -> "Password is invalid"
        else -> null
    }
    message?.let { throw ValidationException(it) }
}