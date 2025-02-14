package ru.point.feature.cars.model

import kotlinx.serialization.Serializable

@Serializable
data class ModelResponse(
    val id: Int,
    val name: String,
    val brandName: String,
)