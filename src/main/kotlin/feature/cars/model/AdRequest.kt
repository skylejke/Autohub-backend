package ru.point.feature.cars.model

import kotlinx.serialization.Serializable

@Serializable
data class AdRequest(
    val car: CarRequest,
    val photos: List<String>
)
