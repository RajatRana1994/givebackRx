package com.app.givebackrx.data.entity

data class SuccessModelResponse(
    val auth: AuthSuccess,
    val date: DateSuccess,
    val message: String,
    val success: Boolean
)

data class AuthSuccess(
    val new_jwt_token: String
)

data class DateSuccess(
    val address_id: String,
    val hold_response: Boolean
)