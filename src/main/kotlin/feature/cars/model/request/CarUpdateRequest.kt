package feature.cars.model.request

import kotlinx.serialization.Serializable
import database.cars.model.CarUpdateRequestDto

@Serializable
data class CarUpdateRequest(
    val year: Short? = null,
    val price: Int? = null,
    val mileage: Int? = null,
    val enginePower: Short? = null,
    val engineCapacity: Double? = null,
    val fuelType: String? = null,
    val bodyType: String? = null,
    val color: String? = null,
    val transmission: String? = null,
    val driveTrain: String? = null,
    val wheel: String? = null,
    val condition: String? = null,
    val owners: Byte? = null,
    val vin: String? = null,
    val ownershipPeriod: String? = null,
    val description: String? = null
)

val CarUpdateRequest.asCarUpdateRequestDto
    get() = CarUpdateRequestDto(
        year = year,
        price = price,
        mileage = mileage,
        engineCapacity = engineCapacity,
        enginePower = enginePower,
        fuelType = fuelType,
        bodyType = bodyType,
        color = color,
        transmission = transmission,
        driveTrain = driveTrain,
        wheel = wheel,
        condition = condition,
        owners = owners,
        vin = vin,
        ownershipPeriod = ownershipPeriod,
        description = description
    )