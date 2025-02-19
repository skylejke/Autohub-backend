package database.models

import database.brands.BrandsTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object ModelsTable : Table("models") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val brandId = integer("brand_id").references(BrandsTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)

    fun getModelsByBrandName(brandName: String) = transaction {
        (ModelsTable innerJoin BrandsTable)
            .select { BrandsTable.name.lowerCase() eq brandName.lowercase() }
            .map {
                ModelResponseDto(
                    id = it[ModelsTable.id],
                    name = it[name],
                    brandName = it[BrandsTable.name]
                )
            }
    }
}