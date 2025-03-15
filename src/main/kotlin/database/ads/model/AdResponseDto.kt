package database.ads.model

import database.ads.AdsTable
import database.car_ad_photos.CarAdsPhotosTable
import database.cars.model.CarResponseDto
import database.cars.model.asCarResponseDto
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import java.time.LocalDate

data class AdResponseDto(
    val id: String,
    val creationDate: LocalDate,
    val userId: String,
    val car: CarResponseDto,
    val photos: List<String>
)

val ResultRow.asAdResponseDto
    get() = AdResponseDto(
        id = get(AdsTable.id),
        creationDate = get(AdsTable.creationDate),
        userId = get(AdsTable.userId),
        car = asCarResponseDto,
        photos = CarAdsPhotosTable
            .slice(CarAdsPhotosTable.id)
            .select { CarAdsPhotosTable.carAdId eq get(AdsTable.id) }
            .map { it[CarAdsPhotosTable.id] }
    )