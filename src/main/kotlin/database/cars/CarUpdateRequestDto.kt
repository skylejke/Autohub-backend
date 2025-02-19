package database.cars

data class CarUpdateRequestDto(
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