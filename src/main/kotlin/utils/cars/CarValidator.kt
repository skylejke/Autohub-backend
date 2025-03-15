package utils.cars

import feature.cars.model.request.AdRequest
import utils.ValidationException
import utils.cars.enums.*

fun AdRequest.validate() {
    with(car) {
        val message = when {
            !bodyType.isValidBodyType() -> "Body type is invalid"
            !condition.isValidCondition() -> "Condition is invalid"
            !drivetrain.isValidDrivetrain() -> "Drivetrain is invalid"
            !fuelType.isValidFuelType() -> "Fuel type is invalid"
            !transmission.isValidTransmission() -> "Transmission is invalid"
            !wheel.isValidWheel() -> "Wheel is invalid"
            !year.isValidYear() -> "Year is invalid"
            !price.isValidPrice() -> "Price is invalid"
            !mileage.isValidMileage() -> "Mileage is invalid"
            !enginePower.isValidPower() -> "Power is invalid"
            !engineCapacity.isValidCapacity() -> "Capacity is invalid"
            else -> null
        }
        message?.let { throw ValidationException(it) }
    }
}

fun String.isValidBodyType() = BodyTypes.entries.any { it.toString() == this }

fun String.isValidCondition() = Conditions.entries.any { it.toString() == this }

fun String.isValidDrivetrain() = Drivetrains.entries.any { it.toString() == this }

fun String.isValidFuelType() = FuelTypes.entries.any { it.toString() == this }

fun String.isValidTransmission() = Transmissions.entries.any { it.toString() == this }

fun String.isValidWheel() = Wheels.entries.any { it.toString() == this }

fun Short.isValidYear() = this in 1885..2030

fun Int.isValidPrice() = this > 0

fun Int.isValidMileage() = this >= 0

fun Short.isValidPower() = this in 1..9999

fun Double.isValidCapacity() = this in 0.1..100.0