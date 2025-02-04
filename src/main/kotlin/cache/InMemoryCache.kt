package cache

import ru.point.feature.authorization.model.Token
import ru.point.feature.authorization.register.model.RegisterRequest


object InMemoryCache {
    val userList: MutableList<RegisterRequest> = mutableListOf()
    val tokenList: MutableList<Token> = mutableListOf()
}