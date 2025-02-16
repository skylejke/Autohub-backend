package ru.point.feature.cars.model

import kotlinx.serialization.Serializable
import ru.point.database.brands.BrandResponseDto

@Serializable
data class BrandResponse(
    val id: Int,
    val name: String,
)

val BrandResponseDto.asBrandResponse: BrandResponse
    get() = BrandResponse(
        id = id,
        name = name,
    )