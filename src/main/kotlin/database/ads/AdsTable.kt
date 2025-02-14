package database.ads

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.transaction
import ru.point.database.ads.AdResponseDto
import ru.point.database.brands.BrandsTable
import ru.point.database.cars.CarFilters
import ru.point.database.cars.CarsTable
import ru.point.database.models.ModelsTable
import ru.point.database.users.UsersTable
import ru.point.utils.toAdResponseDto
import ru.point.utils.toOpCondition

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

        val joinedQuery = AdsTable
            .innerJoin(CarsTable)
            .innerJoin(ModelsTable)
            .join(BrandsTable, JoinType.INNER, onColumn = ModelsTable.brandId, otherColumn = BrandsTable.id)
            .select { searchOp }

        joinedQuery.map { it.toAdResponseDto() }
    }

    fun getAdById(adId: String): AdResponseDto? = transaction {
        val row = AdsTable
            .innerJoin(CarsTable)
            .innerJoin(ModelsTable)
            .join(BrandsTable, JoinType.INNER, onColumn = ModelsTable.brandId, otherColumn = BrandsTable.id)
            .select { AdsTable.id eq adId }
            .singleOrNull()

        row?.toAdResponseDto()
    }

    fun getAdsByFilters(filters: CarFilters): List<AdResponseDto> = transaction {
        val finalCondition = filters.toOpCondition()

        AdsTable
            .innerJoin(CarsTable)
            .innerJoin(ModelsTable)
            .join(BrandsTable, JoinType.INNER, onColumn = ModelsTable.brandId, otherColumn = BrandsTable.id)
            .select { finalCondition }
            .map {
                it.toAdResponseDto()
            }
    }
}

