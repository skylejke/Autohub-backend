package ru.point.utils.cars

class BrandNotFoundException(brand: String): IllegalArgumentException("Brand '$brand' not found")