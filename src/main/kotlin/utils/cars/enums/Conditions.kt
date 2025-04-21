package utils.cars.enums

enum class Conditions(private val alternativeType: String? = null) {
    NOT_BROKEN("Not broken"),
    BROKEN,
    REQUIRES_REPAIR("Requires repair"), ;

    override fun toString(): String = alternativeType ?: name.lowercase().replaceFirstChar { it.uppercase() }
}