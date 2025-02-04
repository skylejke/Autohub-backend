package ru.point.feature.authorization.model

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Token(val token: String)