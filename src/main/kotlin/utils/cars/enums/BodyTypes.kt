package ru.point.utils.cars.enums

enum class BodyTypes(private val alternativeType: String? = null) {
    SEDAN,
    COUPE,
    STATION_WAGON("Station Wagon (Estate)"),
    HATCHBACK,
    MINIVAN,
    PICKUP_TRUCK("Pickup Truck"),
    LIMOUSINE,
    VAN,
    CONVERTIBLE,
    LIFTBACK,
    SUV,
    TRUCK, ;

    override fun toString(): String = alternativeType ?: name.lowercase().replaceFirstChar { it.uppercase() }
}