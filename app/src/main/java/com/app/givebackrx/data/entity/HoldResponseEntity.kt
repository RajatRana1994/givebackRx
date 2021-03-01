package com.app.givebackrx.data.entity

data class HoldResponseEntity(
    val auth: AuthHold,
    val data: DataHold,
    val message: String,
    val success: Boolean
)

data class AuthHold(
    val new_jwt_token: String
)

data class DataHold(
    val payment_response: Boolean,
    val order_id: String
)