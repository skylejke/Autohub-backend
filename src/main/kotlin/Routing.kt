package ru.point

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.point.database.users.UsersTable

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("This is AutoHub")
        }
        get("/users") {
            call.respond(HttpStatusCode.OK, if (UsersTable.getAllUsers().isNotEmpty()) "Yes" else "No")
        }
    }
}
