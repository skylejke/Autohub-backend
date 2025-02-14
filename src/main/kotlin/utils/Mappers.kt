package ru.point.utils

import database.ads.AdsTable
import database.car_ad_photos.CarAdsPhotosTable
import io.ktor.http.*
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import ru.point.database.ads.AdResponseDto
import ru.point.database.brands.BrandDto
import ru.point.database.brands.BrandsTable
import ru.point.database.cars.CarFilters
import ru.point.database.cars.CarResponseDto
import ru.point.database.cars.CarsTable
import ru.point.database.models.ModelDto
import ru.point.database.models.ModelsTable
import ru.point.feature.cars.model.AdResponse
import ru.point.feature.cars.model.BrandResponse
import ru.point.feature.cars.model.CarResponse
import ru.point.feature.cars.model.ModelResponse

fun BrandDto.mapToBrandResponse() =
    BrandResponse(
        id = id,
        name = name,
    )

fun ModelDto.mapToModelResponse(): ModelResponse =
    ModelResponse(
        id = id,
        name = name,
        brandName = brandName,
    )

fun AdResponseDto.mapToAdResponse() =
    AdResponse(
        id = id,
        userId = userId,
        creationDate = creationDate,
        car = with(car) {
            CarResponse(
                id = id,
                brand = brand,
                model = model,
                year = year,
                price = price,
                engineCapacity = engineCapacity,
                enginePower = enginePower,
                fuelType = fuelType,
                mileage = mileage,
                bodyType = bodyType,
                color = color,
                transmission = transmission,
                drivetrain = drivetrain,
                wheel = wheel,
                condition = condition,
                owners = owners,
                vin = vin,
                ownershipPeriod = ownershipPeriod,
                description = description,
            )
        },
        photos = photos
    )

fun Parameters.mapToCarFilters() =
    CarFilters(
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

fun ResultRow.toCarResponseDto(): CarResponseDto = CarResponseDto(
    id = this[CarsTable.id],
    brand = this[BrandsTable.name],
    model = this[ModelsTable.name],
    year = this[CarsTable.year],
    price = this[CarsTable.price],
    mileage = this[CarsTable.mileage],
    enginePower = this[CarsTable.enginePower],
    engineCapacity = this[CarsTable.engineCapacity],
    fuelType = this[CarsTable.fuelType],
    bodyType = this[CarsTable.bodyType],
    color = this[CarsTable.color],
    transmission = this[CarsTable.transmission],
    drivetrain = this[CarsTable.drivetrain],
    wheel = this[CarsTable.wheel],
    condition = this[CarsTable.condition],
    owners = this[CarsTable.owners],
    vin = this[CarsTable.vin],
    ownershipPeriod = this[CarsTable.ownershipPeriod],
    description = this[CarsTable.description]
)

fun ResultRow.toAdResponseDto(): AdResponseDto {
    val adId = this[AdsTable.id]
    return AdResponseDto(
        id = adId,
        creationDate = this[AdsTable.creationDate],
        userId = this[AdsTable.userId],
        car = this.toCarResponseDto(),
        photos = CarAdsPhotosTable
            .select { CarAdsPhotosTable.carAdId eq adId }
            .map { photoRow -> photoRow[CarAdsPhotosTable.uri] }
    )
}

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