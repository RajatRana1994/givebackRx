package com.app.givebackrx.data.entity

data class CharityDetailEntity(
    val auth:Auth,
    val `data`: CharityDetailData,
    val message: String,
    val success: Boolean
)

data class CharityDetailData(
    val card_detail: CardDetail,
    val charity_detail: CharityDetail
)

data class CardDetail(
    val bin: String,
    val charity_name: String,
    val member_name: String?="",
    val group: String,
    val member_type: String,
    val member_id: String?,
    val pcn: String
)

data class CharityDetail(
    val category: String,
    val charity_address: String,
    val charity_city: String,
    val charity_id: String,
    val charity_logo: String,
    val charity_name: String,
    val charity_state: String,
    val check_should_made: String,
    val description: String,
    val donation_by_you: String,
    val donation_from_givebackrx: String,
    val federal_id_number: String,
    val preferred_payment_method: String,
    val selected: Boolean,
    val share_url: String,
    val status_501: String,
    val website: String
)