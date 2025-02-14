package ru.point.utils.cars.enums


enum class FuelTypes {
    PETROL,
    DIESEL,
    HYBRID,
    ELECTRIC, ;

    override fun toString(): String = name.lowercase().replaceFirstChar { it.uppercase() }
}