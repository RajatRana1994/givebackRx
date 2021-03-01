package com.app.givebackrx.data.entity

data class StoreDetailEntity(
    val auth: Auth,
    val `data`: StoreDetailData,
    val message: String,
    val success: Boolean
)

data class StoreDetailData(
    val product_detail: ProductDetail,
    val similar_products: List<SimilarProduct>,
    val total_cart_items: String
)

data class ProductDetail(
    val category: String,
    val chest: String,
    val color: String,
    val color_code: String,
    val colors: List<String>,
    val description: String,
    val discount_percent: String,
    val discount_price: String,
    val display_product_name: String,
    val images: List<String>,
    val maximum_retail_price: String,
    val product_group: String,
    val product_id: String,
    val product_name: String,
    val size: String,
    val size_array: List<SizeArray>,
    val sizes: List<String>
)

data class SimilarProduct(
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

data class SizeArray(
    val available: Boolean,
    val chest_size: String,
    val size: String
)