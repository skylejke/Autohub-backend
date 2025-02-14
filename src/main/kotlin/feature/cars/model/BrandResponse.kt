package ru.point.feature.cars.model

import kotlinx.serialization.Serializable

@Serializable
data class BrandResponse(
    val id: Int,
    val name: String,
)