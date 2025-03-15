package database.users

import database.users.model.RegisterRequestDto
import database.users.model.UpdateUsersDataRequestDto
import database.users.model.UpdateUsersPasswordRequestDto
import database.users.model.UsersDataResponseDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.transactions.transaction
import utils.authorization.UserNotFoundException

object UsersTable : Table("users") {
    val id = varchar("id", 50)
    private val username = varchar("username", 20)
    private val email = varchar("email", 50)
    private val phoneNumber = varchar("phoneNumber", 50)
    private val password = varchar("password", 50)

    fun insert(registerRequestDto: RegisterRequestDto) = transaction {
        insert {
            it[id] = registerRequestDto.id
            it[username] = registerRequestDto.username
            it[email] = registerRequestDto.email
            it[phoneNumber] = registerRequestDto.phoneNumber
            it[password] = registerRequestDto.password
        }
    }

    fun getUserByUsername(userName: String): UsersDataResponseDto = transaction {
        val user = UsersTable.select { username eq userName }.singleOrNull() ?: throw UserNotFoundException()
        UsersDataResponseDto(
            id = user[UsersTable.id],
            username = user[username],
            email = user[email],
            phoneNumber = user[phoneNumber],
            password = user[password]
        )
    }

    fun getAllUsers(): List<UsersDataResponseDto> = transaction {
        UsersTable.selectAll().map {
            UsersDataResponseDto(
                id = it[UsersTable.id],
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
            id = user[UsersTable.id],
            username = user[username],
            email = user[email],
            phoneNumber = user[phoneNumber],
            password = user[password]
        )
    }

    fun updateUsersData(userId: String, updateUsersDataRequestDto: UpdateUsersDataRequestDto) = transaction {
        updateById(userId) { builder ->
            with(updateUsersDataRequestDto) {
                username?.let { builder[UsersTable.username] = it }
                email?.let { builder[UsersTable.email] = it }
                phoneNumber?.let { builder[UsersTable.phoneNumber] = it }
            }
        }
    }

    fun updateUsersPassword(userId: String, updateUsersPasswordRequestDto: UpdateUsersPasswordRequestDto) = transaction {
        updateById(userId) { builder ->
            updateUsersPasswordRequestDto.password.let { builder[password] = it }
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
