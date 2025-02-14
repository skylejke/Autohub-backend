package ru.point.utils.cars.enums

enum class Conditions(private val alternativeType: String? = null) {
    NOT_BROKEN("Not broken"),
    BROKEN,
    DOES_NOT_MATTER("Does not matter"),
    REQUIRES_REPAIR("Requires repair"), ;

    override fun toString(): String = alternativeType ?: name.lowercase().replaceFirstChar { it.uppercase() }
}