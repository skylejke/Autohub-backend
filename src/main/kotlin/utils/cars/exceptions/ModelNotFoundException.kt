package utils.cars.exceptions

class ModelNotFoundException(brand: String, model: String) :
    IllegalArgumentException("Model '$model' for brand '$brand' not found")