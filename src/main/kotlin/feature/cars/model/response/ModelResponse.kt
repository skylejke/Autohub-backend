package feature.cars.model.response

import kotlinx.serialization.Serializable
import database.models.model.ModelResponseDto

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
