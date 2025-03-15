package feature.cars.service

import database.ads.AdsTable
import database.ads.model.asAdRequestDto
import database.brands.BrandsTable
import database.car_ad_photos.CarAdsPhotosTable
import database.cars.model.asCarFilters
import database.favourites.FavouritesTable
import database.models.ModelsTable
import feature.cars.model.request.*
import feature.cars.model.response.asAdResponse
import feature.cars.model.response.asBrandResponse
import feature.cars.model.response.asModelResponse
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import utils.ValidationException
import utils.cars.validate

object CarsService {

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

        var carJson: String? = null
        val photoBytesList = mutableListOf<ByteArray>()

        call.receiveMultipart().forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    if (part.name == "car") {
                        carJson = part.value
                    }
                }

                is PartData.FileItem -> {
                    if (part.name == "photo") {
                        photoBytesList.add(part.provider().readRemaining().readByteArray())
                    }
                }

                else -> {
                    call.respond(HttpStatusCode.BadRequest, "Wrong part data")
                }
            }
            part.dispose()
        }

        if (carJson == null) {
            return call.respond(HttpStatusCode.BadRequest, "Missing car data")
        }

        val carRequest: CarRequest = try {
            Json.decodeFromString(carJson!!)
        } catch (e: Exception) {
            return call.respond(HttpStatusCode.BadRequest, "Invalid car data format: ${e.localizedMessage}")
        }

        val adRequest = AdRequest(car = carRequest, photos = photoBytesList)

        try {
            adRequest.validate()
            AdsTable.insertAd(userId, adRequest.asAdRequestDto)
            call.respond(HttpStatusCode.Created, "Successfully created new car ad")
        } catch (e: ValidationException) {
            return call.respond(HttpStatusCode.BadRequest, e.message ?: "Validation error")
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
        val adId =
            call.parameters["adId"]?.lowercase() ?: return call.respond(HttpStatusCode.BadRequest, "Ad id is required")

        var carJson: String? = null
        val newPhotoBytesList = mutableListOf<ByteArray>()
        var removePhotoIds: List<String> = emptyList()

        call.receiveMultipart().forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "car" -> carJson = part.value
                        "removePhotoIds" -> {
                            removePhotoIds = try {
                                Json.decodeFromString(part.value)
                            } catch (e: Exception) {
                                part.value.split(",").map { it.trim() }
                            }
                        }
                    }
                }

                is PartData.FileItem -> {
                    if (part.name == "newPhotos") {
                        newPhotoBytesList.add(part.provider().readRemaining().readByteArray())
                    }
                }

                else -> {
                    call.respond(HttpStatusCode.BadRequest, "Wrong part data")
                }
            }
            part.dispose()
        }

        val carUpdateRequest: CarUpdateRequest? = carJson?.let {
            try {
                Json.decodeFromString<CarUpdateRequest>(it)
            } catch (e: Exception) {
                return call.respond(HttpStatusCode.BadRequest, "Invalid car update data: ${e.localizedMessage}")
            }
        }

        val adUpdateRequest = AdUpdateRequest(
            car = carUpdateRequest,
            newPhotos = newPhotoBytesList.ifEmpty { null },
            removePhotosIds = removePhotoIds.ifEmpty { null }
        )

        try {
            AdsTable.updateAdById(userId, adId, adUpdateRequest.asAdUpdateRequestDto)
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

    suspend fun getPhoto(call: ApplicationCall) {
        val photoId = call.parameters["photoId"]
            ?: return call.respond(HttpStatusCode.BadRequest, "Photo id is required")

        val photoBytes = transaction {
            CarAdsPhotosTable
                .select { CarAdsPhotosTable.id eq photoId }
                .singleOrNull()
                ?.get(CarAdsPhotosTable.photo)
        }

        if (photoBytes == null) {
            call.respond(HttpStatusCode.NotFound, "Photo not found")
        } else {
            call.respondBytes(photoBytes, ContentType.Image.JPEG)
        }
    }
}