package ru.point.database.ads

import ru.point.database.cars.CarResponseDto
import java.time.LocalDate

data class AdResponseDto(
    val id: String,
    val creationDate: LocalDate,
    val userId: String,
    val car: CarResponseDto,
    val photos: List<String>
)
