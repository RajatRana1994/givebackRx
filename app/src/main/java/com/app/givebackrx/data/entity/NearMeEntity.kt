package com.app.givebackrx.data.entity

data class NearMeEntity(
    val auth:Auth,
    val `data`: List<NearMeData>,
    val message: String,
    val success: Boolean
)

data class NearMeData(
    val Address: String,
    val City: String,
    val Distance: String,
    val Latitude: String,
    val Longitude: String,
    val NABP: String,
    val NPI: String,
    val Name: String,
    val Phone: String,
    val State: String,
    val ZipCode: String
)