package utils.cars.exceptions

class BrandNotFoundException(brand: String): IllegalArgumentException("Brand '$brand' not found")