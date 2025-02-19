package ru.point.feature.authorization.login.model

import kotlinx.serialization.Serializable
import feature.authorization.common.Token

@Serializable
data class LoginResponse(val token: Token)