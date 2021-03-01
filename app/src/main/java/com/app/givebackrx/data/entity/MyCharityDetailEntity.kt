package com.app.givebackrx.data.entity

data class MyCharityDetailEntity(
    val auth:Auth,
    val `data`: MyCharityDetailData,
    val message: String,
    val success: Boolean
)

data class MyCharityDetailData(
    val card_detail: CardDetail,
    val charity_detail: MyCharityDetail,
    val new_jwt_token: String
)


data class MyCharityDetail(
    val additional_image: String,
    val category: String,
    val charity_address: String,
    val charity_address_cheque: String,
    val charity_city: String,
    val charity_city_cheque: String,
    val charity_id: String,
    val charity_logo: String,
    val charity_main_phone: String,
    val charity_name: String,
    val charity_state: String,
    val charity_state_cheque: String,
    val charity_zip_code: Long,
    val charity_zip_code_cheque: Long,
    val checking_account_number: Long,
    val description: String,
    val donation_from_givebackrx: String,
    val federal_id_number: String,
    val last_updated_date: String,
    val preferred_payment_method: String,
    val routing_number: Long,
    val signee_email: String,
    val signee_first_name: String,
    val signee_last_name: String,
    val signee_organizational_role: String,
    val signee_phone: String,
    val signee_secondary_email: String,
    val signee_secondary_phone: String,
    val status: String,
    val status_501: String,
    val user_type: String,
    val website: String
)