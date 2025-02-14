package ru.point.utils

import io.ktor.http.*


data class ValidationError(val httpStatusCode: HttpStatusCode, val message: String)