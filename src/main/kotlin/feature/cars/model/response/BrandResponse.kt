package feature.cars.model.response

import kotlinx.serialization.Serializable
import database.brands.BrandResponseDto

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