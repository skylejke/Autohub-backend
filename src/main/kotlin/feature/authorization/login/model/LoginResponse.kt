package ru.point.feature.authorization.login.model

import kotlinx.serialization.Serializable
import ru.point.feature.authorization.model.Token

@Serializable
data class LoginResponse(val token: Token)