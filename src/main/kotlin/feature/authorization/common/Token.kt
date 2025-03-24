package feature.authorization.common

import kotlinx.serialization.Serializable

@Serializable
data class Token(val token: String)