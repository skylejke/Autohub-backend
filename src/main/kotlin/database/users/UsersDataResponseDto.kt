package database.users

data class UsersDataResponseDto(
    val id: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String
)
