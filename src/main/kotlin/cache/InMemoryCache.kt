package cache

import ru.point.feature.register.model.RegisterRequest

data class TokenCache(
    val username: String,
    val token: String
)

object InMemoryCache {
    val userList: MutableList<RegisterRequest> = mutableListOf()
    val tokenList: MutableList<TokenCache> = mutableListOf()
}