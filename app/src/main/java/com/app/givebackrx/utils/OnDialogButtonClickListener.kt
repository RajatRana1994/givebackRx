package com.app.givebackrx.utils

interface OnDialogButtonClickListener {
    fun onPositiveButtonClicked()
    fun onNegativeButtonClicked()
    fun onNeutralButtonClicked()
}

interface NetworkConstants {

    companion object {
        const val SUCCESS = 200
        const val AUTHFAIL = 401
        const val ERROR404 = 404



        const val drug= "drug"
        const val coupon= "coupon"
        const val couponViaEmail= "couponViaEmail"
        const val textCard= "textCard"
        const val couponViaPhone= "couponViaPhone"
        const val dashboard="dashboard"
        const val resend="resend"
        const val invitebyReferral="invitebyReferral"
        const val goldCardPayment="goldCardPayment"
        const val paymentCard="paymentCard"
        const val referral="referral"//referral_status=All&user_type
        const val notices="notices"//notices_status=All&user_type
        const val feedback="feedback"//notices_status=All&user_type
        const val task="task"//task_status=Active, user_type
        const val myCharity= "myCharity"
        const val profileDetail= "profileDetail"
        const val editProfileImage= "editProfileImage"
        const val editProfile= "editProfile"
        const val profileDetailVerifyOTP= "profileDetailVerifyOTP"
        const val pharmacyNearMe= "pharmacyNearMe"
        const val appVersion= "appVersion"
        const val faq= "faq"
        const val emailCard= "emailCard"
        const val address= "address"
        const val payment= "payment"
        const val charityCardList= "charityCardList"
        const val defaultAddress= "defaultAddress"
        const val pharmacyList= "pharmacyList"
        const val preSignUpVerification= "preSignUpVerification"
        const val postSignUpVerification= "postSignUpVerification"
        const val signUp= "signUp"
        const val signIn= "signIn"
        const val forgotPassword= "forgotPassword"
        const val verifyOTP= "verifyOTP"
        const val sendAgain= "sendAgain"
        const val charity= "charity"
        const val charityDetail= "charityDetail"
        const val registeredCharityDetail= "registeredCharityDetail"
        const val selectCharity= "selectCharity"
        const val profilePassword= "profilePassword"
        const val profileEnableAuth= "profileEnableAuth"
        const val profileSecurity= "profileSecurity"
        const val notificationPreference= "notificationPreference"
        const val updateCard= "updateCard"
        const val store= "store"
        const val myCart= "myCart"
        const val disableRecurringPayment= "disableRecurringPayment"
        const val getCart= "getCart"
        const val storeProductDetail= "storeProductDetail"
        const val favoriteMedication= "favoriteMedication"
        const val recentPurchases= "recentPurchases"
        const val goldSummary= "membershipOrderSummary"
        const val storeSummary= "orderSummary"
        const val paymentCards= "paymentCard"
        const val selectPaymentCard= "selectPaymentCard"
        const val storePayment= "storePayment"
        const val storePaymentResponse= "storePaymentResponse"
    }
}

interface PreferenceConstants {
    companion object {
        val SharedPrefenceName = "GBRxApp"
        val USER_LOGGED_IN= "user_logged"
        val HOWITWORK= "HOWITWORK"
        val USER_LOGO= "USER_LOGO"
        val USER_ID= "USER_ID"
        val USER_TYPE= "USER_TYPE"
        val USER_NAME= "USER_NAME"
        val MEMBER_TYPE= "MEMBER_TYPE"
        val REFERRAL= "REFERRAL"
        val DEVICE_TOKEN= "device_token"
        val LANGUAGE_TYPE= "language_type"
        val ACCESSTOKEN= "accesstoken"
        val BASE_URL= "BASE_URL"
        val GotToken= "GotToken"
        val TOKENTYPE= "TOKENTYPE"
        val USER_DATA= "userData"
        val FREQ_SEARCH= "FREQ_SEARCH"
        val CHARITY= "CHARITY"
        val LAT= "lat"
        val LNG= "lng"
        val LANGUAGE= "language"


    }
}