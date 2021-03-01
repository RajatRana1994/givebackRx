package com.app.givebackrx.data.entity

data class CartEntity(
    val auth: Auth,
    val `data`: CartData,
    val message: String,
    val success: Boolean
)

data class CartData(
    val cart_item_list: List<CartItem>,
    val catalog_order_id: String,
    var default_address: DefaultAddress,
    val is_recurring_payment_active: Boolean,
    var order_summary: OrderSummary,
    val saved_cards: List<SavedCard>,
    val total_cart_items: Int
)

data class CartItem(
    val category: String="",
    val color: String="",
    val discounted_price: String="",
    val display_product_name: String="",
    val image_url: String="",
    val price: String="",
    val product_group: String="",
    val product_id: String="",
    val product_name: String="",
    var quantity: String="",
    val size: String="",
    val sizes: List<String>
)

data class DefaultAddress(
    val address: String,
    val address_id: String,
    val address_type: String,
    val apt_suit_number: String,
    val city: String,
    val country: String,
    var default_address: Boolean,
    val phone: String,
    val registered_address: Boolean,
    val user_type: String="",
    val state: String,
    val zip_code: Int
)

data class OrderSummary(
    val discount: String,
    val estimated_tax: String,
    val final_amount: String,
    val gbrx_discount_coupon_amount: String,
    val gbrx_discount_coupon_name: String,
    val gbrx_discount_coupon_description: String,
    val shipping: String,
    val subtotal: String,
    val total: String
)

data class SavedCard(
    val billing_city: String,
    val billing_country: String,
    val billing_state: String,
    val billing_street: String,
    val billing_zip_code: String,
    val card_id: String,
    val card_number: String,
    val card_type: String,
    val expiry_month: String,
    val expiry_year: String,
    val first_name: String,
    val last_name: String,
    val recurring_payment: Boolean
)