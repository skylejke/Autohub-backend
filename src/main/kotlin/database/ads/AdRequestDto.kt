package ru.point.database.ads

import ru.point.database.cars.CarRequestDto
import ru.point.database.cars.asCarRequestDto
import ru.point.feature.cars.model.AdRequest

data class AdRequestDto(
    val car: CarRequestDto,
    val photos: List<String>
)

val AdRequest.asAdRequestDto
    get() = AdRequestDto(
        car = car.asCarRequestDto,
        photos = photos
    )