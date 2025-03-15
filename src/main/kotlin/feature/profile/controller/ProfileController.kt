package feature.profile.controller

import feature.profile.service.ProfileService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureProfileController() {
    routing {
        get("/profile/{userId}") {
            ProfileService.getUsersData(call)
        }

        patch("/profile/{userId}") {
            ProfileService.updateUsersData(call)
        }

        patch("/profile/{userId}/updatePassword") {
            ProfileService.updateUserPassword(call)
        }

        delete("/profile/{userId}") {
            ProfileService.deleteUser(call)
        }
    }
}