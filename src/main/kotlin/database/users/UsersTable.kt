package ru.point.database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object UsersTable : Table("users") {
    val id = varchar("id", 50)
    private val username = varchar("username", 20)
    private val email = varchar("email", 50)
    private val phoneNumber = varchar("phoneNumber", 50)
    private val password = varchar("password", 50)

    fun insert(userDto: UserDto) {
        transaction {
            insert {
                it[id] = UUID.randomUUID().toString()
                it[username] = userDto.username
                it[email] = userDto.email
                it[phoneNumber] = userDto.phoneNumber
                it[password] = userDto.password
            }
        }
    }

    fun getUserByUsername(userName: String): UserDto? {
        return transaction {
            val user = UsersTable.select { username eq userName }.singleOrNull() ?: return@transaction null
            UserDto(
                username = user[username],
                email = user[email],
                phoneNumber = user[phoneNumber],
                password = user[password]
            )
        }
    }

    fun getAllUsers(): List<UserDto> {
        return transaction {
            UsersTable.selectAll().map {
                UserDto(
                    username = it[username],
                    email = it[email],
                    phoneNumber = it[phoneNumber],
                    password = it[password]
                )
            }
        }
    }
}
