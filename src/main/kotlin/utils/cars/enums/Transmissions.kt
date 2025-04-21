package utils.cars.enums


enum class Transmissions(private val alternativeType: String? = null) {
    AUTOMATIC,
    ROBOTIZED,
    CVT,
    MANUAL, ;

    override fun toString(): String = alternativeType ?: name.lowercase().replaceFirstChar { it.uppercase() }
}