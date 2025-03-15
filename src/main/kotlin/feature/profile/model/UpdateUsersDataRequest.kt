package feature.profile.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUsersDataRequest(
    val username: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
)
