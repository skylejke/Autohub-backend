package feature.cars.model.response

import kotlinx.serialization.Serializable
import ru.point.database.ads.AdResponseDto
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

val AdResponseDto.asAdResponse
    get() = AdResponse(
        id = id,
        userId = userId,
        creationDate = creationDate,
        car = with(car) {
            CarResponse(
                id = id,
                brand = brand,
                model = model,
                year = year,
                price = price,
                engineCapacity = engineCapacity,
                enginePower = enginePower,
                fuelType = fuelType,
                mileage = mileage,
                bodyType = bodyType,
                color = color,
                transmission = transmission,
                drivetrain = drivetrain,
                wheel = wheel,
                condition = condition,
                owners = owners,
                vin = vin,
                ownershipPeriod = ownershipPeriod,
                description = description,
            )
        },
        photos = photos
    )