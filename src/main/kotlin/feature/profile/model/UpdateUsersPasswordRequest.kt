package feature.profile.model

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class UpdateUsersPasswordRequest(val password: String)