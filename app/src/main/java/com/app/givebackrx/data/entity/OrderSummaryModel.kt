package com.app.givebackrx.data.entity

data class OrderSummaryModel(
        val auth: AuthOrder,
        val `data`: DataOrder,
        val message: String,
        val success: Boolean
)

data class AuthOrder(
        val new_jwt_token: String
)

data class DataOrder(
        val default_address: DefaultAddressOrder,
        val estimated_tax: String,
        val is_recurring_payment_active: Boolean,
        val order_summary: OrderSummaryOrder,
        val saved_cards: List<Any>,
        val shipping: String
)

data class DefaultAddressOrder(
        val address: String,
        val address_id: String,
        val address_type: String,
        val apt_suit_number: String,
        val city: String,
        val country: String,
        val default_address: Boolean,
        val phone: String,
        val registered_address: Boolean,
        val state: String,
        val zip_code: Int
)

data class OrderSummaryOrder(
        val coupon_description: String? = "",
        val discount: String,
        val final_amount: String,
        val gbrx_discount_coupon_name: String,
        val subtotal: String
)