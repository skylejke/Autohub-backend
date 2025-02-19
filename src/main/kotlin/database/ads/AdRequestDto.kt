package database.ads

import database.cars.CarRequestDto
import database.cars.asCarRequestDto
import feature.cars.model.request.AdRequest

data class AdRequestDto(
    val car: CarRequestDto,
    val photos: List<String>
)

val AdRequest.asAdRequestDto
    get() = AdRequestDto(
        car = car.asCarRequestDto,
        photos = photos
    )