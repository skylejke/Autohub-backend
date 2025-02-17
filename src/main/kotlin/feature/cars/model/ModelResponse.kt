package ru.point.feature.cars.model

import kotlinx.serialization.Serializable
import ru.point.database.models.ModelResponseDto

@Serializable
data class ModelResponse(
    val id: Int,
    val name: String,
    val brandName: String,
)

val ModelResponseDto.asModelResponse
    get() = ModelResponse(
        id = id,
        name = name,
        brandName = brandName,
    )
