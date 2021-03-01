package com.app.givebackrx.data.entity

data class StoreOrderSummary(
    val auth: Authee,
    val `data`: Dataa,
    val message: String,
    val success: Boolean
)

data class Authee(
    val new_jwt_token: String
)

data class Dataa(

    val hold_response: Boolean,
    val shopping_type: String?=null,
    val billing_address: BillingAddress?=null,
    val order_summary: OrderSummaryy,
    val payment_method: PaymentMethod?=null,
    val product_list: ProductList,
    val shipping_address: ShippingAddress?=null,
    val shipping_method_list: List<ShippingMethodList>?=null
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

data class OrderSummaryy(
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
    val first_name: String?=null,
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

data class ShippingMethodList(
        val id: String,
        val shipping_rate: String,
        val lable: String,
        val estimated_delivery_date: String,
        var selected: Boolean

)

data class Item(
    val discounted_price: String,
    val display_product_name: String,
    val image_url: String,
    val maximum_retail_price: String,
    val product_id: String,
    val product_name: String,
    val quantity: String
)