package com.app.givebackrx.appcode.orderSummary

data class OrderSummaryModel(
    val auth: Auth,
    val `data`: Data,
    val message: String,
    val success: Boolean
)

data class Auth(
    val new_jwt_token: String
)

data class Data(
    val billing_address: BillingAddress,
    val order_summary: OrderSummary,
    val payment_method: List<PaymentMethod>,
    val product_list: ProductList,
    val shipping_address: ShippingAddress,
    val shipping_method_list: List<Any>
)

data class BillingAddress(
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

data class OrderSummary(
    val coupon_description: String,
    val coupon_discount: String,
    val price_tag_discount: String,
    val shipping: String,
    val sub_total: String,
    val tax: String,
    val total: String
)

data class PaymentMethod(
    val card_id: String,
    val card_number: String,
    val card_type: String,
    val expiry_month: String,
    val expiry_year: String,
    val first_name: String,
    val last_name: String,
    val selected_card: Boolean
)

data class ProductList(
    val catalog_order_id: String,
    val count: Int,
    val items: List<Item>
)

data class ShippingAddress(
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

data class Item(
    val discounted_price: String,
    val display_product_name: String,
    val image_url: String,
    val maximum_retail_price: String,
    val product_id: String,
    val product_name: String,
    val quantity: String,
    val size: String
)