package database.cars

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateStatement
import database.ads.AdRequestDto
import database.brands.BrandsTable
import database.models.ModelsTable
import utils.cars.exceptions.BrandNotFoundException
import utils.cars.exceptions.ModelNotFoundException
import java.util.*


object CarsTable : Table("cars") {
    val id = varchar("id", 50).clientDefault { UUID.randomUUID().toString() }
    private val brandId = integer("brand_id").references(BrandsTable.id, onDelete = ReferenceOption.CASCADE)
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

    fun insertCar(carId: String, adRequestDto: AdRequestDto) {
        val brandRow = BrandsTable
            .select { BrandsTable.name eq adRequestDto.car.brand }
            .singleOrNull() ?: throw BrandNotFoundException(adRequestDto.car.brand)

        val modelRow = ModelsTable
            .select { (ModelsTable.name eq adRequestDto.car.model) and (ModelsTable.brandId eq brandRow[BrandsTable.id]) }
            .singleOrNull()
            ?: throw ModelNotFoundException(adRequestDto.car.brand, adRequestDto.car.model)

        CarsTable.insert {
            it[id] = carId
            it[brandId] = brandRow[BrandsTable.id]
            it[modelId] = modelRow[ModelsTable.id]
            it[year] = adRequestDto.car.year
            it[price] = adRequestDto.car.price
            it[mileage] = adRequestDto.car.mileage
            it[bodyType] = adRequestDto.car.bodyType
            it[enginePower] = adRequestDto.car.enginePower
            it[engineCapacity] = adRequestDto.car.engineCapacity
            it[fuelType] = adRequestDto.car.fuelType
            it[color] = adRequestDto.car.color
            it[transmission] = adRequestDto.car.transmission
            it[drivetrain] = adRequestDto.car.drivetrain
            it[wheel] = adRequestDto.car.wheel
            it[condition] = adRequestDto.car.condition
            it[owners] = adRequestDto.car.owners
            it[vin] = adRequestDto.car.vin
            it[ownershipPeriod] = adRequestDto.car.ownershipPeriod
            it[description] = adRequestDto.car.description
        }
    }

    fun updateById(id: String, body: CarsTable.(UpdateStatement) -> Unit) {
        CarsTable.update({ CarsTable.id eq id }) {
            body.invoke(this, it)
        }
    }
}