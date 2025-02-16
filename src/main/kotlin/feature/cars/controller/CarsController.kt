package ru.point.feature.cars.controller

import database.ads.AdsTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.point.database.ads.asAdRequestDto
import ru.point.database.brands.BrandsTable
import ru.point.database.cars.asCarFilters
import ru.point.database.favourites.FavouritesTable
import ru.point.database.models.ModelsTable
import ru.point.feature.cars.model.*

object CarsController {

    suspend fun getAllBrands(call: ApplicationCall) {
        val brandResponses = BrandsTable.getBrands().map { it.asBrandResponse }
        call.respond(HttpStatusCode.OK, brandResponses)
    }

    suspend fun getModelsByBrand(call: ApplicationCall) {
        val brandName = call.parameters["brandName"]?.lowercase() ?: return call.respond(
            HttpStatusCode.BadRequest,
            "Brand id is required"
        )
        val modelResponses = ModelsTable.getModelsByBrandName(brandName).map { it.asModelResponse }
        call.respond(HttpStatusCode.OK, modelResponses)
    }

    suspend fun getAdsByQuery(call: ApplicationCall) {
        val query = call.request.queryParameters["query"].orEmpty()
        val adResponses = AdsTable.findAds(query).map { it.asAdResponse }
        call.respond(HttpStatusCode.OK, adResponses)
    }

    suspend fun getAdById(call: ApplicationCall) {
        val adId = call.parameters["adId"]?.lowercase() ?: return call.respond(
            HttpStatusCode.BadRequest,
            "Ad id is required"
        )
        val adResponse = AdsTable.getAdById(adId)?.asAdResponse
        call.respond(HttpStatusCode.OK, adResponse ?: "Ad not found")
    }

    suspend fun getAdsByFilters(call: ApplicationCall) {
        val params = call.request.queryParameters
        val filters = params.asCarFilters
        val adResponses = AdsTable.getAdsByFilters(filters).map { it.asAdResponse }
        call.respond(HttpStatusCode.OK, adResponses)
    }

    suspend fun getUsersAds(call: ApplicationCall) {
        val userId = call.parameters["userId"]?.lowercase() ?: return call.respond(
            HttpStatusCode.BadRequest,
            "User id is required"
        )
        val adResponses = AdsTable.getUsersAds(userId).map { it.asAdResponse }
        call.respond(HttpStatusCode.OK, adResponses.ifEmpty { "You have no ads" })
    }

    suspend fun createNewAd(call: ApplicationCall) {
        val userId = call.parameters["userId"]?.lowercase() ?: return call.respond(
            HttpStatusCode.BadRequest,
            "User id is required"
        )
        val request = call.receive<AdRequest>()
        try {
            AdsTable.insertAd(
                userId = userId,
                adRequestDto = request.asAdRequestDto
            )
            call.respond(HttpStatusCode.Created, "Successfully created new car ad")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Can't create ad: ${e.localizedMessage}")
        }
    }

    suspend fun deleteAdById(call: ApplicationCall) {
        val userId = call.parameters["userId"]?.lowercase() ?: return call.respond(
            HttpStatusCode.BadRequest,
            "User id is required"
        )
        val adId = call.parameters["adId"]?.lowercase() ?: return call.respond(
            HttpStatusCode.BadRequest,
            "Ad id is required"
        )
        try {
            AdsTable.deleteAdById(userId, adId)
            call.respond(HttpStatusCode.OK, "Successfully deleted car ad")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Can't delete ad: ${e.localizedMessage}")
        }
    }

    suspend fun updateAdById(call: ApplicationCall) {
        val userId = call.parameters["userId"]?.lowercase() ?: return call.respond(
            HttpStatusCode.BadRequest,
            "User id is required"
        )
        val adId = call.parameters["adId"]?.lowercase() ?: return call.respond(
            HttpStatusCode.BadRequest,
            "Ad id is required"
        )

        val request = call.receive<CarUpdateRequest>()

        try {
            AdsTable.updateAdById(userId, adId, request.asCarUpdateRequestDto)
            call.respond(HttpStatusCode.OK, "Successfully updated car ad")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Can't update ad: ${e.localizedMessage}")
        }
    }

    suspend fun addAdToFavourites(call: ApplicationCall) {
        val userId = call.parameters["userId"]?.lowercase() ?: return call.respond(
            HttpStatusCode.BadRequest,
            "User id is required"
        )
        val adId = call.parameters["adId"]?.lowercase() ?: return call.respond(
            HttpStatusCode.BadRequest,
            "Ad id is required"
        )

        try {
            FavouritesTable.insertAd(userId, adId)
            call.respond(HttpStatusCode.OK, "Successfully created new favourite ad")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Can't add to favourites favourite ad: ${e.localizedMessage}")
        }
    }

    suspend fun removeAdFromFavourites(call: ApplicationCall) {
        val userId = call.parameters["userId"]?.lowercase() ?: return call.respond(
            HttpStatusCode.BadRequest,
            "User id is required"
        )
        val adId = call.parameters["adId"]?.lowercase() ?: return call.respond(
            HttpStatusCode.BadRequest,
            "Ad id is required"
        )

        try {
            FavouritesTable.deleteAd(userId, adId)
            call.respond(HttpStatusCode.OK, "Successfully removed car ad from favourites")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Can't remove ad from favourites: ${e.localizedMessage}")
        }
    }

    suspend fun getUsersFavourites(call: ApplicationCall) {
        val userId = call.parameters["userId"]?.lowercase() ?: return call.respond(
            HttpStatusCode.BadRequest,
            "User id is required"
        )
        val adResponses = FavouritesTable.getFavoriteAds(userId).map { it.asAdResponse }
        call.respond(HttpStatusCode.OK, adResponses)
    }
}