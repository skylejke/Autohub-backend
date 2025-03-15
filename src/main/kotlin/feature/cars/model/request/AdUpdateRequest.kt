package feature.cars.model.request

import database.ads.model.AdUpdateRequestDto
import kotlinx.serialization.Serializable

@Serializable
data class AdUpdateRequest(
    val car: CarUpdateRequest? = null,
    val newPhotos: List<ByteArray>? = null,
    val removePhotosIds: List<String>? = null,
)

val AdUpdateRequest.asAdUpdateRequestDto
    get() = AdUpdateRequestDto(
        car = car?.asCarUpdateRequestDto,
        newPhotos = newPhotos,
        removePhotosIds = removePhotosIds
    )