package utils.profile

import feature.profile.model.request.UpdateUsersDataRequest
import feature.profile.model.request.UpdateUsersPasswordRequest
import utils.ValidationException
import utils.common.isValidEmail
import utils.common.isValidPassword
import utils.common.isValidPhoneNumber
import utils.common.isValidUserName

fun UpdateUsersDataRequest.validate() {
    val message = when {
        this.username?.isValidUserName() == false -> "Username is invalid"
        this.email?.isValidEmail() == false -> "Email is invalid"
        this.phoneNumber?.isValidPhoneNumber() == false -> "Phone number is invalid"
        else -> null
    }
    message?.let { throw ValidationException(it) }
}

fun UpdateUsersPasswordRequest.validate() {
    val message = when {
        !this.newPassword.isValidPassword() -> "New password is invalid"
        this.newPassword != this.confirmNewPassword -> "New password and confirmation do not match"
        this.oldPassword.isBlank() -> "Old password must be provided"
        else -> null
    }
    message?.let { throw ValidationException(it) }
}