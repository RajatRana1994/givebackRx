package com.app.givebackrx.data.entity

data class ProfileEntity(
    val auth:Auth,
    val `data`: ProfileData,
    val message: String,
    val success: Boolean
)

data class ProfileData(
    var address: String?,
    var apt_suite_number: String?,
    var cell_number: String?,
    var city: String?,
    var country: String?,
    val dob: String?,
    var email: String?,
    var first_name: String?,
    var last_name: String?,
    var name: String?,
    var postal_code: String?,
    val profile_image: String?,
    var secondary_cell_number: String?,
    var secondary_email: String?,
    var state: String?,
    val user_type: String?
)


data class VerifySecondaryOtpEntity(
    val `data`: VerifySecondaryOtpData,
    val message: String,
    val success: Boolean
)

data class VerifySecondaryOtpData(
    val redisOTP: Int,
    val user_type: String
)



data class ProfileUpdatedEntity(
    val auth: Auth,
    val `data`: ProfileUpdatedData,
    val message: String,
    val success: Boolean
)

data class Auth(
    val new_jwt_token: String
)

data class ProfileUpdatedData(
    val pic_url: String,
    val user_type: String
)