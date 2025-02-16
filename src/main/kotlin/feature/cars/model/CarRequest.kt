package ru.point.feature.cars.model

import kotlinx.serialization.Serializable

@Serializable
data class CarRequest(
    val brand: String,
    val model: String,
    val year: Short,
    val price: Int,
    val mileage: Int,
    val enginePower: Short,
    val engineCapacity: Double,
    val fuelType: String,
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