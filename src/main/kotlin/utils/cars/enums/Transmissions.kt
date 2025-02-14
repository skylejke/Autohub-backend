package ru.point.utils.cars.enums


enum class Transmissions(private val alternativeType: String? = null) {
    AUTOMATIC,
    ROBOTIZED,
    CONTINUOUSLY_VARIABLE ("Continuously variable"),
    MANUAL, ;

    override fun toString(): String = alternativeType ?: name.lowercase().replaceFirstChar { it.uppercase() }
}