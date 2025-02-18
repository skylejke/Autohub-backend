package feature.authorization.register.model

import kotlinx.serialization.Serializable
import ru.point.feature.authorization.model.Token

@Serializable
data class RegisterResponse(val token: Token)