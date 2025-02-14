package ru.point.database.brands

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object BrandsTable : Table("brands") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255).uniqueIndex()

    override val primaryKey = PrimaryKey(id)

    fun getBrands(): List<BrandDto> = transaction {
        selectAll().map {
            BrandDto(
                id = it[BrandsTable.id],
                name = it[name],
            )
        }
    }
}