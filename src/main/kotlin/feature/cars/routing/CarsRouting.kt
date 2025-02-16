package ru.point.feature.cars.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.point.feature.cars.controller.CarsController

fun Application.configureCarsRouting() {
    routing {
        get("/brands") {
            CarsController.getAllBrands(call)
        }

        get("/models/{brandName}") {
            CarsController.getModelsByBrand(call)
        }

        get("/cars") {
            CarsController.getAdsByQuery(call)
        }

        get("/cars/{adId}") {
            CarsController.getAdById(call)
        }

        get("/cars/filters") {
            CarsController.getAdsByFilters(call)
        }

        get("/profile/{userId}/ads") {
            CarsController.getUsersAds(call)
        }

        post("/profile/{userId}/ads") {
            CarsController.createNewAd(call)
        }

        delete("/profile/{userId}/ads/{adId}") {
            CarsController.deleteAdById(call)
        }

        patch("/profile/{userId}/ads/{adId}") {
            CarsController.updateAdById(call)
        }

        post("/profile/{userId}/favourites/{adId}") {
            CarsController.addAdToFavourites(call)
        }

        delete("/profile/{userId}/favourites/{adId}") {
            CarsController.removeAdFromFavourites(call)
        }

        get("/profile/{userId}/favourites") {
            CarsController.getUsersFavourites(call)
        }
    }
}