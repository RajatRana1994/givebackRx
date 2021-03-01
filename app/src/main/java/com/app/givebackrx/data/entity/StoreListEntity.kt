package com.app.givebackrx.data.entity

data class StoreListEntity(
    val auth: Auth,
    val `data`: StoreListData,
    val message: String,
    val success: Boolean
)

data class StoreListData(
    val pages: List<Int>,
    val products: List<StoreProduct>,
    val total_cart_items: String
)

data class StoreProduct(
    val active: Boolean,
    val category: String,
    val color: String,
    val description: String,
    val discount_percent: String,
    val discount_price: String,
    val display_product_name: String,
    val display_url: String,
    val maximum_retail_price: String,
    val product_group: String,
    val product_id: String,
    val product_name: String,
    val size: String
)