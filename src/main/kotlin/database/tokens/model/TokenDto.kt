package database.tokens.model

import feature.authorization.common.Token

@JvmInline
value class TokenDto(
    val token: Token
)