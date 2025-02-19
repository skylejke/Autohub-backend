package feature.authorization.register.model

import kotlinx.serialization.Serializable
import feature.authorization.common.Token

@Serializable
data class RegisterResponse(val token: Token)