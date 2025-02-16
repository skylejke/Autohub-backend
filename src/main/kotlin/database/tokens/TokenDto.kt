package ru.point.database.tokens

import ru.point.feature.authorization.model.Token

@JvmInline
value class TokenDto(
    val token: Token
)