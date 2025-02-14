package ru.point.database.cars

data class CarFilters(
    val brand: String? = null,
    val model: String? = null,
    val yearMin: Short? = null,
    val yearMax: Short? = null,
    val priceMin: Int? = null,
    val priceMax: Int? = null,
    val mileageMin: Int? = null,
    val mileageMax: Int? = null,
    val enginePowerMin: Short? = null,
    val enginePowerMax: Short? = null,
    val engineCapacityMin: Double? = null,
    val engineCapacityMax: Double? = null,
    val fuelType: String? = null,
    val bodyType: String? = null,
    val color: String? = null,
    val transmission: String? = null,
    val drivetrain: String? = null,
    val wheel: String? = null,
    val condition: String? = null,
    val owners: Byte? = null
)