package database.comparisons

import database.ads.AdsTable
import database.ads.model.AdResponseDto
import database.ads.model.asAdResponseDto
import database.brands.BrandsTable
import database.cars.CarsTable
import database.models.ModelsTable
import database.users.UsersTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object ComparisonsTable : Table("comparisons") {
    private val userId = varchar("user_id", 50).references(UsersTable.id, onDelete = ReferenceOption.CASCADE)
    private val adId = varchar("ad_id", 36).references(AdsTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(userId, adId)

    fun insertAdToComparisons(userId: String, adId: String) = transaction {
        ComparisonsTable.insert {
            it[ComparisonsTable.userId] = userId
            it[ComparisonsTable.adId] = adId
        }
    }

    fun deleteAdFromComparisons(userId: String, adId: String) = transaction {
        ComparisonsTable.deleteWhere {
            (ComparisonsTable.userId eq userId) and (ComparisonsTable.adId eq adId)
        }
    }

    fun getComparisonsList(userId: String): List<AdResponseDto> = transaction {
        ComparisonsTable
            .innerJoin(AdsTable, { adId }, { id })
            .innerJoin(CarsTable, { AdsTable.carId }, { id })
            .innerJoin(ModelsTable, { CarsTable.modelId }, { id })
            .innerJoin(BrandsTable, { ModelsTable.brandId }, { id })
            .select { ComparisonsTable.userId eq userId }
            .map { row -> row.asAdResponseDto }
    }
}