package utils.common

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