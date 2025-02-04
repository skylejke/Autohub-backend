package ru.point

import io.ktor.server.application.*
import ru.point.feature.authorization.login.routing.configureLoginRouting
import ru.point.feature.authorization.register.routing.configureRegisterRouting

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureLoginRouting()
    configureRegisterRouting()
}
