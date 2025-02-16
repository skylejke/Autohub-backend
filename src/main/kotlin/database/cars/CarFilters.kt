package ru.point.database.cars

import io.ktor.http.*
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.and
import ru.point.database.brands.BrandsTable
import ru.point.database.models.ModelsTable

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
    val owners: Byte? = null
)

val Parameters.asCarFilters
    get() = CarFilters(
        brand = get("brand"),
        model = get("model"),
        yearMin = get("year_min")?.toShortOrNull(),
        yearMax = get("year_max")?.toShortOrNull(),
        priceMin = get("price_min")?.toIntOrNull(),
        priceMax = get("price_max")?.toIntOrNull(),
        mileageMin = get("mileage_min")?.toIntOrNull(),
        mileageMax = get("mileage_max")?.toIntOrNull(),
        enginePowerMin = get("engine_power_min")?.toShortOrNull(),
        enginePowerMax = get("engine_power_max")?.toShortOrNull(),
        engineCapacityMin = get("engine_capacity_min")?.toDoubleOrNull(),
        engineCapacityMax = get("engine_capacity_max")?.toDoubleOrNull(),
        fuelType = get("fuel_type"),
        bodyType = get("body_type"),
        color = get("color"),
        transmission = get("transmission"),
        drivetrain = get("drivetrain"),
        wheel = get("wheel"),
        condition = get("condition"),
        owners = get("owners")?.toByteOrNull()
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
        owners?.let { CarsTable.owners eq it }
    )
    return if (conditions.isNotEmpty()) conditions.reduce { acc, op -> acc and op } else Op.TRUE
}