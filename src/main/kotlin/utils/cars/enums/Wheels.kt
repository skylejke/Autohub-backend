package utils.cars.enums


enum class Wheels() {
    RIGHT,
    LEFT, ;

    override fun toString(): String = name.lowercase().replaceFirstChar { it.uppercase() }
}