package utils.common

import database.users.UsersTable
import utils.authorization.EmailIsTakenException
import utils.authorization.PhoneNumberIsTakenException
import utils.authorization.UserNameIsTakenException

fun checkDataAvailability(username: String?, email: String?, phoneNumber: String?) {
    val users = UsersTable.getAllUsers()
    when {
        users.map { it.username }
            .contains(username) -> throw UserNameIsTakenException()

        users.map { it.email }
            .contains(email) -> throw EmailIsTakenException()

        users.map { it.phoneNumber }
            .contains(phoneNumber) -> throw PhoneNumberIsTakenException()
    }
}