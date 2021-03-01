package com.app.givebackrx.data.entity

data class MyCartEntity(
    val auth: Auth,
    val message: String,
    val order_summary: OrderSummary?=null,
    val success: Boolean,
    val total_cart_items: String
)
