package feature.cars.controller

import feature.cars.service.CarsService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureCarsController() {
    routing {
        get("/brands") {
            CarsService.getAllBrands(call)
        }

        get("/models/{brandName}") {
            CarsService.getModelsByBrand(call)
        }

        get("/cars") {
            CarsService.getAdsByQuery(call)
        }

        get("/cars/{adId}") {
            CarsService.getAdById(call)
        }

        get("/cars/filters") {
            CarsService.getAdsByFilters(call)
        }

        get("/profile/{userId}/ads") {
            CarsService.getUsersAds(call)
        }

        post("/profile/{userId}/ads") {
            CarsService.createNewAd(call)
        }

        delete("/profile/{userId}/ads/{adId}") {
            CarsService.deleteAdById(call)
        }

        patch("/profile/{userId}/ads/{adId}") {
            CarsService.updateAdById(call)
        }

        post("/profile/{userId}/favourites/{adId}") {
            CarsService.addAdToFavourites(call)
        }

        delete("/profile/{userId}/favourites/{adId}") {
            CarsService.removeAdFromFavourites(call)
        }

        get("/profile/{userId}/favourites") {
            CarsService.getUsersFavourites(call)
        }

        get("/photos/{photoId}") {
            CarsService.getPhoto(call)
        }

        post("profile/{userId}/comparisons/{adId}") {
            CarsService.insertAdToComparisons(call)
        }

        delete("profile/{userId}/comparisons/{adId}") {
            CarsService.removeAdFromComparisons(call)
        }

        get("profile/{userId}/comparisons") {
            CarsService.getUsersComparisons(call)
        }
    }
}