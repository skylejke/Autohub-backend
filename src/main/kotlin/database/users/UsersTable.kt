package database.users

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.transactions.transaction
import utils.authorization.UserNotFoundException
import java.util.*

object UsersTable : Table("users") {
    val id = varchar("id", 50)
    private val username = varchar("username", 20)
    private val email = varchar("email", 50)
    private val phoneNumber = varchar("phoneNumber", 50)
    private val password = varchar("password", 50)

    fun insert(registerRequestDto: RegisterRequestDto) = transaction {
        insert {
            it[id] = UUID.randomUUID().toString()
            it[username] = registerRequestDto.username
            it[email] = registerRequestDto.email
            it[phoneNumber] = registerRequestDto.phoneNumber
            it[password] = registerRequestDto.password
        }
    }

    fun getUserByUsername(userName: String): RegisterRequestDto = transaction {
        val user = UsersTable.select { username eq userName }.singleOrNull() ?: throw UserNotFoundException()
        RegisterRequestDto(
            username = user[username],
            email = user[email],
            phoneNumber = user[phoneNumber],
            password = user[password]
        )
    }

    fun getAllUsers(): List<RegisterRequestDto> = transaction {
        UsersTable.selectAll().map {
            RegisterRequestDto(
                username = it[username],
                email = it[email],
                phoneNumber = it[phoneNumber],
                password = it[password]
            )
        }
    }

    fun getUserById(id: String): UsersDataResponseDto = transaction {
        val user = UsersTable.select { UsersTable.id eq id }.singleOrNull() ?: throw UserNotFoundException()
        UsersDataResponseDto(
            username = user[username],
            email = user[email],
            phoneNumber = user[phoneNumber],
        )
    }

    fun updateUsersData(userId: String, updateUsersDataRequestDto: UpdateUsersDataRequestDto) = transaction {
        updateById(userId) { builder ->
            with(updateUsersDataRequestDto) {
                username?.let { builder[UsersTable.username] = it }
                email?.let { builder[UsersTable.email] = it }
                phoneNumber?.let { builder[UsersTable.phoneNumber] = it }
                password?.let { builder[UsersTable.password] = it }
            }
        }
    }

    fun deleteUserById(userId: String) = transaction {
        UsersTable.deleteWhere { id eq userId }
    }


    private fun updateById(id: String, body: UsersTable.(UpdateStatement) -> Unit) {
        UsersTable.update({ UsersTable.id eq id }) {
            body.invoke(this, it)
        }
    }
}
