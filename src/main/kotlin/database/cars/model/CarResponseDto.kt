package database.cars.model

import org.jetbrains.exposed.sql.ResultRow
import database.brands.BrandsTable
import database.cars.CarsTable
import database.models.ModelsTable

data class CarResponseDto(
    val id: String,
    val brand: String,
    val model: String,
    val year: Short,
    val price: Int,
    val enginePower: Short,
    val engineCapacity: Double,
    val fuelType: String,
    val mileage: Int,
    val bodyType: String,
    val color: String,
    val transmission: String,
    val drivetrain: String,
    val wheel: String,
    val condition: String,
    val owners: Byte,
    val vin: String,
    val ownershipPeriod: String,
    val description: String,
)

val ResultRow.asCarResponseDto
    get() = with(this) {
        CarResponseDto(
            id = get(CarsTable.id),
            brand = get(BrandsTable.name),
            model = get(ModelsTable.name),
            year = get(CarsTable.year),
            price = get(CarsTable.price),
            mileage = get(CarsTable.mileage),
            enginePower = get(CarsTable.enginePower),
            engineCapacity = get(CarsTable.engineCapacity),
            fuelType = get(CarsTable.fuelType),
            bodyType = get(CarsTable.bodyType),
            color = get(CarsTable.color),
            transmission = get(CarsTable.transmission),
            drivetrain = get(CarsTable.drivetrain),
            wheel = get(CarsTable.wheel),
            condition = get(CarsTable.condition),
            owners = get(CarsTable.owners),
            vin = get(CarsTable.vin),
            ownershipPeriod = get(CarsTable.ownershipPeriod),
            description = get(CarsTable.description)
        )
    }