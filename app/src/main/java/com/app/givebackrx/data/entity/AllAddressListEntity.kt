package com.app.givebackrx.data.entity

data class AllAddressListEntity(
    val auth: Auth,
    val `data`: List<DefaultAddress>,
    val message: String,
    val success: Boolean
)
