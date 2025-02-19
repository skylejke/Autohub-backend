package database.cars

import feature.cars.model.request.CarRequest

data class CarRequestDto(
    val brand: String,
    val model: String,
    val year: Short,
    val price: Int,
    val enginePower: Short,
    val engineCapacity: Double,
    val fuelType: String,
    val mileage: Int,
    val bodyType: String,
    val color: String,
    val transmission: String,
    val drivetrain: String,
    val wheel: String,
    val condition: String,
    val owners: Byte,
    val vin: String,
    val ownershipPeriod: String,
    val description: String,
)

val CarRequest.asCarRequestDto
    get() = CarRequestDto(
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