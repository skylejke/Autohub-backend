package feature.authorization.common

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Token(val token: String)