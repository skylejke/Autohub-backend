package ru.point.feature.cars.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.point.feature.cars.controller.CarsController

fun Application.configureCarsRouting() {
    routing {

        get("/brands") {
            CarsController(call).getAllBrands()
        }

        get("/models/{brandName}") {
            CarsController(call).getModelsByBrand()
        }


        get("/cars") {
            CarsController(call).getAdsByQuery()
        }

        get("/cars/{adId}") {
            CarsController(call).getAdById()
        }

        get("/cars/filters") {
            CarsController(call).getAdsByFilters()
        }
    }
}