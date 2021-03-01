package com.app.givebackrx.data.entity

data class ReferralEntity(
    val auth:Auth,
    val `data`: ReferralData,
    val message: String,
    val success: Boolean
)

data class ReferralData(
    val referrals: List<ReferralItem>,
    val total_accepted: Int,
    val total_pending: Int,
    val total_referral: Int
)

data class ReferralItem(
    val email: String,
    val name: String,
    val phone: String,
    val referral_send_on: String,
    val referred_by: String,
    val resend_button: Boolean,
    val resend_button_id: String,
    val serial_number: Int,
    val status: String
)