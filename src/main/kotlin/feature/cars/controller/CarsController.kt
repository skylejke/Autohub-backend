package ru.point.feature.cars.controller

import database.ads.AdsTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.point.database.brands.BrandsTable
import ru.point.database.models.ModelsTable
import ru.point.utils.mapToAdResponse
import ru.point.utils.mapToBrandResponse
import ru.point.utils.mapToCarFilters
import ru.point.utils.mapToModelResponse

class CarsController(private val call: ApplicationCall) {

    suspend fun getAllBrands() {
        val brandsDto = BrandsTable.getBrands()
        val brandResponses = brandsDto.map { it.mapToBrandResponse() }
        call.respond(HttpStatusCode.OK, brandResponses)
    }

    suspend fun getModelsByBrand() {
        val brandName = call.parameters["brandName"]?.lowercase() ?: return call.respond(
            HttpStatusCode.BadRequest,
            "Brand id is required"
        )
        val modelsDto = ModelsTable.getModelsByBrandName(brandName = brandName)
        val modelResponses = modelsDto.map { it.mapToModelResponse() }
        call.respond(HttpStatusCode.OK, modelResponses)
    }

    suspend fun getAdsByQuery() {
        val query = call.request.queryParameters["query"] ?: ""
        val adResponseDtos = AdsTable.findAds(query)
        val adResponses = adResponseDtos.map { it.mapToAdResponse() }
        call.respond(HttpStatusCode.OK, adResponses)
    }

    suspend fun getAdById() {
        val adId = call.parameters["adId"]?.lowercase() ?: return call.respond(
            HttpStatusCode.BadRequest,
            "Ad id is required"
        )
        val adResponseDto = AdsTable.getAdById(adId)
        val adResponse = adResponseDto?.mapToAdResponse()
        call.respond(HttpStatusCode.OK, adResponse ?: "Ad not found")
    }

    suspend fun getAdsByFilters() {
        val params = call.request.queryParameters
        val filters = params.mapToCarFilters()
        val adResponseDtos = AdsTable.getAdsByFilters(filters)
        val adResponses = adResponseDtos.map { it.mapToAdResponse() }
        call.respond(HttpStatusCode.OK, adResponses)
    }
}