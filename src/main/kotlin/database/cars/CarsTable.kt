package ru.point.database.cars

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import ru.point.database.brands.BrandsTable
import ru.point.database.models.ModelsTable
import java.util.*


object CarsTable : Table("cars") {
    val id = varchar("id", 50).clientDefault { UUID.randomUUID().toString() }
    val brandId = integer("brand_id").references(BrandsTable.id, onDelete = ReferenceOption.CASCADE)
    val modelId = integer("model_id").references(ModelsTable.id, onDelete = ReferenceOption.CASCADE)
    val year = short("year")
    val price = integer("price")
    val mileage = integer("mileage")
    val bodyType = varchar("body_type", 255)
    val enginePower = short("engine_power")
    val engineCapacity = double("engine_capacity")
    val fuelType = varchar("fuel_type", 50)
    val color = varchar("color", 255)
    val transmission = varchar("transmission", 255)
    val drivetrain = varchar("drivetrain", 255)
    val wheel = varchar("wheel", 255)
    val condition = varchar("condition", 255)
    val owners = byte("owners")
    val vin = varchar("vin", 255).uniqueIndex()
    val ownershipPeriod = varchar("ownership_period", 255)
    val description = varchar("description", 1000)

    override val primaryKey = PrimaryKey(id)
}