package com.app.givebackrx.data.entity

data class PrintedCharityEntity(
    val auth: Auth,
    val `data`: PrintedCharityData,
    val message: String,
    val success: Boolean
)

data class PrintedCharityData(
    val charities: List<PrintedCharity>,
    val total_cart_items: String
)

data class PrintedCharity(
    val bin: String,
    val charity_name: String,
    val group: String,
    val id: String,
    val image_url: String,
    val member_id: String,
    val pbm_logo: String,
    val pcn: String
)