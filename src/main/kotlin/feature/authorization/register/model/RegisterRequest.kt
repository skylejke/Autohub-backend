package feature.authorization.register.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String
)