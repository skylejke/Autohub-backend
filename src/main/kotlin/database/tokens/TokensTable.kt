package database.tokens

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object TokensTable : Table("tokens") {
    private val id = TokensTable.varchar("id", 50)
    private val token = TokensTable.varchar("token", 256)

    fun insert(tokenDto: TokenDto) {
        transaction {
            insert {
                it[id] = UUID.randomUUID().toString()
                it[token] = tokenDto.token.token
            }
        }
    }
}