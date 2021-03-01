package com.app.givebackrx.data.entity

data class SuccessMessageEntity(
    val auth:Auth,
    val message: String,
    val success: Boolean,
    val data:DataEntity
)

data class DataEntity(
    val next_page:Boolean,
    val favorite_medication_id:String
)

data class PaymentEntity(
    val auth:Auth,
    val message: String,
    val success: Boolean,
    val data:DataEntity
)
data class SecurityDetailEntity(
    val `data`: Data,
    val message: String,
    val success: Boolean
)

data class Data(
    val two_factor_authentication: Boolean,
    val user_type: String
)