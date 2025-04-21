package utils.cars.enums

enum class SortParams {
    PRICE,
    MILEAGE,
    YEAR,
    DATE;

    override fun toString() = name.lowercase()
}