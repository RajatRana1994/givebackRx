package com.app.givebackrx.data.entity


data class SignInWithUserDetailEntity(
    val data: SignInWithUserDetailData,
    val message: String,
    val success: Boolean
)

data class SignInWithUserDetailData(
    var address: String?="",
    val apt_suite_number: String?="",
    var city: String?="",
    var email: String?="",
    var firstname: String?="",
    val jwttoken: String?="",
    var lastname: String?="",
    val membertype: String?="",
    val phone: String?="",
    var postalcode: String?="",
    val profile_pic: String?="",
    val referral_code: String?="",
    var state: String?="",
    val otp: String?="",
    val two_factor_auth: Boolean?=null,
    val user_type: String?=null,
    val selected_charity_name: String?=null
)