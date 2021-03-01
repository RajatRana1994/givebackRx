package com.app.givebackrx.data.entity

data class AddressesListEntity(
    val auth: AuthList,
    val count: Int,
    val `data`: List<DataList>,
    val message: String,
    val success: Boolean,
    val user_type: String
)

data class AuthList(
    val new_jwt_token: String
)

data class DataList(
        val address: String,
        val address_id: String,
        val address_type: String,
        val apt_suit_number: String,
        val billing_address: Boolean,
        val city: String,
        val country: String,
        var default_address: Boolean,
        val first_name: String,
        val last_name: String,
        val phone: String,
        val shipping_address: Boolean,
        val state: String,
        val zip_code: String
)