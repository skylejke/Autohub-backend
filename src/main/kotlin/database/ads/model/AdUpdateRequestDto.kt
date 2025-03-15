package database.ads.model

import database.cars.model.CarUpdateRequestDto

data class AdUpdateRequestDto(
    val car: CarUpdateRequestDto? = null,
    val newPhotos: List<ByteArray>? = null,
    val removePhotosIds: List<String>? = null,
)
