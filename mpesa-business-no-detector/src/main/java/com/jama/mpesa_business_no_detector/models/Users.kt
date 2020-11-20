package com.jama.mpesa_business_no_detector.models

data class Users(
    val id: Int,
    val name: String,
    val company: Company,
    val address: Address
)

data class Company(
    val name: String
)

data class Address(
    val city: String,
    val geo: Geo
)

data class Geo(
    val lat: String,
    val lng: String
)