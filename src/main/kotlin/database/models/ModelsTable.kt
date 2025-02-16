package ru.point.database.models

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.point.database.brands.BrandsTable

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