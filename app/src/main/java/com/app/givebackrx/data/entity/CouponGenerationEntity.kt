package com.app.givebackrx.data.entity

data class CouponGenerationEntity(
    val auth:Auth,
    val `data`: CouponGeneration,
    val message: String,
    val success: Boolean
)

data class CouponGeneration(
    val dosage: String,
    val drug_generic_final_name: String,
    val drug_generic_name: String,
    val drug_name: String,
    val form: String,
    val image_url: String,
    val member_id: String,
    val display_member_id: String,
    val pbm_phone: String,
    val pharmacy_name: String,
    val price: String,
    val isFavorite: String,
    val quantity: String,
    val rxbin: String,
    val is_lowest: String,
    val rxgroup: String,
    val rxpcn: String,
    val pharmacy_street_address: String,
    val pharmacy_city: String,
    val pharmacy_state: String,
    val pharmacy_zip_code: String,
    val distance: String
)