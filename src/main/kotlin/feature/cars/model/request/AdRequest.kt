package feature.cars.model.request

import kotlinx.serialization.Serializable

@Serializable
data class AdRequest(
    val car: CarRequest,
    val photos: List<String>
)
