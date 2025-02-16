package database.ads

import database.car_ad_photos.CarAdsPhotosTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.transaction
import ru.point.database.ads.AdRequestDto
import ru.point.database.ads.AdResponseDto
import ru.point.database.ads.asAdResponseDto
import ru.point.database.brands.BrandsTable
import ru.point.database.cars.CarFilters
import ru.point.database.cars.CarUpdateRequestDto
import ru.point.database.cars.CarsTable
import ru.point.database.cars.toOpCondition
import ru.point.database.models.ModelsTable
import ru.point.database.users.UsersTable
import ru.point.utils.cars.CarAdNotFoundException
import ru.point.utils.cars.validate
import java.time.LocalDate
import java.util.*

object AdsTable : Table("ads") {
    val id = varchar("id", 36)
    val carId = varchar("car_id", 36).references(CarsTable.id, onDelete = ReferenceOption.CASCADE)
    val userId = varchar("user_id", 50).references(UsersTable.id, onDelete = ReferenceOption.CASCADE)
    val creationDate = date("creation_date")

    override val primaryKey = PrimaryKey(id)

    fun findAds(query: String?): List<AdResponseDto> = transaction {
        val searchOp: Op<Boolean> = if (!query.isNullOrBlank()) {
            val searchTerm = "%${query.lowercase()}%"
            (BrandsTable.name.lowerCase() like searchTerm) or
                    (ModelsTable.name.lowerCase() like searchTerm) or
                    (CarsTable.description.lowerCase() like searchTerm)
        } else {
            Op.TRUE
        }
        joinTables()
            .select { searchOp }
            .map { it.asAdResponseDto }
    }

    fun getAdById(adId: String): AdResponseDto? = transaction {
        joinTables()
            .select { AdsTable.id eq adId }
            .singleOrNull()
            ?.asAdResponseDto
    }

    fun getAdsByFilters(filters: CarFilters): List<AdResponseDto> = transaction {
        joinTables()
            .select { filters.toOpCondition() }
            .map { it.asAdResponseDto }
    }

    fun getUsersAds(userId: String): List<AdResponseDto> = transaction {
        joinTables()
            .select { AdsTable.userId eq userId }
            .map { it.asAdResponseDto }
    }

    fun insertAd(userId: String, adRequestDto: AdRequestDto) = transaction {

        adRequestDto.validate()

        val adId = UUID.randomUUID().toString()

        val carId = UUID.randomUUID().toString()

        val creationDate = LocalDate.now()

        CarsTable.insertCar(carId, adRequestDto)

        adRequestDto.photos.forEach { photoUrl ->
            CarAdsPhotosTable.insertPhoto(photoUrl, adId)
        }

        AdsTable.insert {
            it[id] = adId
            it[AdsTable.carId] = carId
            it[AdsTable.userId] = userId
            it[AdsTable.creationDate] = creationDate
        }
    }

    fun deleteAdById(userId: String, adId: String) = transaction {
        val adRow = AdsTable
            .select { (AdsTable.id eq adId) and (AdsTable.userId eq userId) }
            .singleOrNull() ?: throw CarAdNotFoundException()

        val carId = adRow[carId]

        CarAdsPhotosTable.deleteWhere { carAdId eq adId }

        AdsTable.deleteWhere { id eq adId }

        CarsTable.deleteWhere { id eq carId }
    }

    fun updateAdById(userId: String, adId: String, carUpdateRequestDto: CarUpdateRequestDto) = transaction {
        val adRow = AdsTable.select { (AdsTable.id eq adId) and (AdsTable.userId eq userId) }
            .singleOrNull() ?: throw CarAdNotFoundException()

        CarsTable.updateById(adRow[carId]) { builder ->
            with(carUpdateRequestDto) {
                year?.let { builder[CarsTable.year] = it }
                price?.let { builder[CarsTable.price] = it }
                mileage?.let { builder[CarsTable.mileage] = it }
                bodyType?.let { builder[CarsTable.bodyType] = it }
                enginePower?.let { builder[CarsTable.enginePower] = it }
                engineCapacity?.let { builder[CarsTable.engineCapacity] = it }
                fuelType?.let { builder[CarsTable.fuelType] = it }
                color?.let { builder[CarsTable.color] = it }
                transmission?.let { builder[CarsTable.transmission] = it }
                driveTrain?.let { builder[drivetrain] = it }
                wheel?.let { builder[CarsTable.wheel] = it }
                condition?.let { builder[CarsTable.condition] = it }
                owners?.let { builder[CarsTable.owners] = it }
                description?.let { builder[CarsTable.description] = it }
            }
        }
    }

    private fun joinTables() = AdsTable
        .innerJoin(CarsTable, { carId }, { id })
        .innerJoin(ModelsTable, { CarsTable.modelId }, { id })
        .innerJoin(BrandsTable, { ModelsTable.brandId }, { id })
}




