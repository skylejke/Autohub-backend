package utils.cars.enums

enum class OrderParams {
    ASC,
    DESC;

    override fun toString() = name.lowercase()
}