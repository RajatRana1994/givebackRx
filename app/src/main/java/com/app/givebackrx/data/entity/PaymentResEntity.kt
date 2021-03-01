package com.app.givebackrx.data.entity

data class PaymentResEntity(
    val auth: AuthPay,
    val `data`: List<DataPay>,
    val message: String,
    val success: Boolean
)

data class AuthPay(
    val new_jwt_token: String
)

data class DataPay(
        val card_id: String,
        val card_number: String,
        val card_type: String,
        val expiry_month: String,
        val expiry_year: String,
        val first_name: String,
        val last_name: String,
        var selected_card: Boolean
)