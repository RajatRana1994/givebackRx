package com.app.givebackrx.data.entity

data class MemberShipOrderEntity(
    val auth: Auths,
    val `data`: Datas,
    val message: String,
    val success: Boolean
)

data class Auths(
    val new_jwt_token: String
)

data class Datas(
    val default_address: DefaultAddresss,
    val estimated_tax: String,
    val is_recurring_payment_active: Boolean,
    val order_summary: OrderSummarys,
    val saved_cards: List<SavedCards>,
    val shipping: String
)

data class DefaultAddresss(
    val address: String,
    val address_id: String,
    val address_type: String,
    val apt_suit_number: String,
    val billing_address: Boolean,
    val city: String,
    val country: String,
    val default_address: Boolean,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val shipping_address: Boolean,
    val state: String,
    val zip_code: String
)

data class OrderSummarys(
    val coupon_description: String,
    val discount: String,
    val final_amount: String,
    val gbrx_discount_coupon_name: String,
    val subtotal: String
)

data class SavedCards(
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