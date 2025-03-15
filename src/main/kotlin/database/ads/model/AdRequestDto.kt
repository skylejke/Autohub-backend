package database.ads.model

import database.cars.model.CarRequestDto
import database.cars.model.asCarRequestDto
import feature.cars.model.request.AdRequest

data class AdRequestDto(
    val car: CarRequestDto,
    val photos: List<ByteArray>
)

val AdRequest.asAdRequestDto
    get() = AdRequestDto(
        car = car.asCarRequestDto,
        photos = photos
    )