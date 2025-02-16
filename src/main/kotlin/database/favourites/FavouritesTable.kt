package ru.point.database.favourites

import database.ads.AdsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.point.database.ads.AdResponseDto
import ru.point.database.ads.asAdResponseDto
import ru.point.database.brands.BrandsTable
import ru.point.database.cars.CarsTable
import ru.point.database.models.ModelsTable
import ru.point.database.users.UsersTable

object FavouritesTable : Table("favourites") {
    private val userId = varchar("user_id", 50).references(UsersTable.id, onDelete = ReferenceOption.CASCADE)
    private val adId = varchar("ad_id", 36).references(AdsTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(userId, adId)

    fun insertAd(userId: String, adId: String) = transaction {
        FavouritesTable.insert {
            it[FavouritesTable.userId] = userId
            it[FavouritesTable.adId] = adId
        }
    }

    fun deleteAd(userId: String, adId: String) = transaction {
        FavouritesTable.deleteWhere {
            (FavouritesTable.userId eq userId) and (FavouritesTable.adId eq adId)
        }
    }

    fun getFavoriteAds(userId: String): List<AdResponseDto> = transaction {
        FavouritesTable
            .innerJoin(AdsTable, { adId }, { id })
            .innerJoin(CarsTable, { AdsTable.carId }, { id })
            .innerJoin(ModelsTable, { CarsTable.modelId }, { id })
            .innerJoin(BrandsTable, { ModelsTable.brandId }, { id })
            .select { FavouritesTable.userId eq userId }
            .map { row -> row.asAdResponseDto }
    }
}