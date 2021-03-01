package com.app.givebackrx.data.entity

data class MyCharityEntity(
    val auth:Auth,
    val `data`: List<MyCharityData>,
    val message: String,
    val success: Boolean
)

data class MyCharityData(
    val charity_id: String,
    val charity_logo: String,
    val charity_name: String,
    val description: String,
    val donation_from_giveback_enterprises: String,
    val selected: Boolean
)