package utils.authorization

import feature.authorization.register.model.RegisterRequest
import utils.ValidationException
import utils.common.isValidEmail
import utils.common.isValidPassword
import utils.common.isValidPhoneNumber
import utils.common.isValidUserName

fun RegisterRequest.validate() {
    val message = when {
        !this.username.isValidUserName() -> "Username is invalid"
        !this.password.isValidPassword() -> "Password is invalid"
        !this.email.isValidEmail() -> "Email is invalid"
        !this.phoneNumber.isValidPhoneNumber() -> "Phone number is invalid"
        else -> null
    }
    message?.let { throw ValidationException(it) }
}


class UserNameIsTakenException : IllegalArgumentException("Username is taken")

class EmailIsTakenException : IllegalArgumentException("Email is taken")

class PhoneNumberIsTakenException : IllegalArgumentException("Phone number is taken")