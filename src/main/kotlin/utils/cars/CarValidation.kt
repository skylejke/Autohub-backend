package ru.point.utils.cars

import io.ktor.http.*
import ru.point.feature.cars.model.CarRequest
import ru.point.utils.ValidationError
import ru.point.utils.cars.enums.*

fun CarRequest.validate(): ValidationError? =
    when {
        !bodyType.isValidBodyType() -> ValidationError(HttpStatusCode.Forbidden, "Body type is invalid")
        !condition.isValidCondition() -> ValidationError(HttpStatusCode.Forbidden, "Condition is invalid")
        !drivetrain.isValidDrivetrain() -> ValidationError(HttpStatusCode.Forbidden, "Drivetrain is invalid")
        !fuelType.isValidFuelType() -> ValidationError(HttpStatusCode.Forbidden, "Fuel type is invalid")
        !transmission.isValidTransmission() -> ValidationError(HttpStatusCode.Forbidden, "Transmission is invalid")
        !wheel.isValidWheel() -> ValidationError(HttpStatusCode.Forbidden, "Wheel is invalid")
        !year.isValidYear() -> ValidationError(HttpStatusCode.Forbidden, "Year is invalid")
        !price.isValidPrice() -> ValidationError(HttpStatusCode.Forbidden, "Price is invalid")
        !mileage.isValidMileage() -> ValidationError(HttpStatusCode.Forbidden, "Mileage is invalid")
        !enginePower.isValidPower() -> ValidationError(HttpStatusCode.Forbidden, "Power is invalid")
        !engineCapacity.isValidCapacity() -> ValidationError(HttpStatusCode.Forbidden, "Capacity is invalid")
        else -> null
    }

fun String.isValidBodyType() = BodyTypes.entries.any { it.toString() == this }

fun String.isValidCondition() = Conditions.entries.any { it.toString() == this }

fun String.isValidDrivetrain() = Drivetrains.entries.any { it.toString() == this }

fun String.isValidFuelType() = FuelTypes.entries.any { it.toString() == this }

fun String.isValidTransmission() = Transmissions.entries.any { it.toString() == this }

fun String.isValidWheel() = Wheels.entries.any { it.toString() == this }

fun Int.isValidYear() = this in 1885..2030

fun Int.isValidPrice() = this > 0

fun Int.isValidMileage() = this >= 0

fun Short.isValidPower() = this in 1..9999

fun Double.isValidCapacity() = this in 0.1..100.0