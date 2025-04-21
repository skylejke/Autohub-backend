package database.cars.model

import database.brands.BrandsTable
import database.cars.CarsTable
import database.models.ModelsTable
import io.ktor.http.*
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.and

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
    val owners: OwnersOption? = null
)

val Parameters.asCarFilters
    get() = CarFilters(
        brand = get("brand"),
        model = get("model"),
        yearMin = get("yearMin")?.toShortOrNull(),
        yearMax = get("yearMax")?.toShortOrNull(),
        priceMin = get("priceMin")?.toIntOrNull(),
        priceMax = get("priceMax")?.toIntOrNull(),
        mileageMin = get("mileageMin")?.toIntOrNull(),
        mileageMax = get("mileageMax")?.toIntOrNull(),
        enginePowerMin = get("enginePowerMin")?.toShortOrNull(),
        enginePowerMax = get("enginePowerMax")?.toShortOrNull(),
        engineCapacityMin = get("engineCapacityMin")?.toDoubleOrNull(),
        engineCapacityMax = get("engineCapacityMax")?.toDoubleOrNull(),
        fuelType = get("fuelType"),
        bodyType = get("bodyType"),
        color = get("color"),
        transmission = get("transmission"),
        drivetrain = get("drivetrain"),
        wheel = get("wheel"),
        condition = get("condition"),
        owners = parseOwnersOption(get("owners"))
    )

fun CarFilters.toOpCondition(): Op<Boolean> {
    val conditions = listOfNotNull(
        brand?.let { BrandsTable.name eq it },
        model?.let { ModelsTable.name eq it },
        yearMin?.let { CarsTable.year greaterEq it },
        yearMax?.let { CarsTable.year lessEq it },
        priceMin?.let { CarsTable.price greaterEq it },
        priceMax?.let { CarsTable.price lessEq it },
        mileageMin?.let { CarsTable.mileage greaterEq it },
        mileageMax?.let { CarsTable.mileage lessEq it },
        enginePowerMin?.let { CarsTable.enginePower greaterEq it },
        enginePowerMax?.let { CarsTable.enginePower lessEq it },
        engineCapacityMin?.let { CarsTable.engineCapacity greaterEq it },
        engineCapacityMax?.let { CarsTable.engineCapacity lessEq it },
        fuelType?.let { CarsTable.fuelType eq it },
        bodyType?.let { CarsTable.bodyType eq it },
        color?.let { CarsTable.color eq it },
        transmission?.let { CarsTable.transmission eq it },
        drivetrain?.let { CarsTable.drivetrain eq it },
        wheel?.let { CarsTable.wheel eq it },
        condition?.let { CarsTable.condition eq it },
        owners?.let { option ->
            when (option) {
                OwnersOption.ZERO -> CarsTable.owners eq 0
                OwnersOption.ONE -> CarsTable.owners eq 1
                OwnersOption.NO_MORE_THAN_TWO -> CarsTable.owners lessEq 2
                OwnersOption.NO_MORE_THAN_THREE -> CarsTable.owners lessEq 3
            }
        }
    )
    return if (conditions.isNotEmpty()) conditions.reduce { acc, op -> acc and op } else Op.TRUE
}

enum class OwnersOption(val label: String) {
    ZERO("Zero"),
    ONE("One"),
    NO_MORE_THAN_TWO("No more than two"),
    NO_MORE_THAN_THREE("No more than three")
}

fun parseOwnersOption(param: String?) =
    OwnersOption.entries.find { it.label.equals(param, ignoreCase = true) }

