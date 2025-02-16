package ru.point.utils.cars

class ModelNotFoundException(brand: String, model: String) :
    IllegalArgumentException("Model '$model' for brand '$brand' not found")