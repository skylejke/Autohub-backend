package feature.profile.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUsersPasswordRequest(
    val oldPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)