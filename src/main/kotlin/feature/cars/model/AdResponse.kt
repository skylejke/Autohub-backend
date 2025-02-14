package ru.point.feature.cars.model

import kotlinx.serialization.Serializable
import ru.point.utils.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class AdResponse(
    val id: String,
    @Serializable(with = LocalDateSerializer::class)
    val creationDate: LocalDate,
    val userId: String,
    val car: CarResponse,
    val photos: List<String>
)