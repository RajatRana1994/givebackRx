package com.app.givebackrx.data.entity

data class RecentPurchaseEntity(
    val auth: Auth,
    val count: Int,
    val `data`: RecentPurchaseData,
    val message: String,
    val success: Boolean
)


data class RecentPurchaseData(
    val medication: List<String>,
    val pharmacy: List<String>,
    val purchases: List<RecentPurchase>,
    val time: List<String>
)

data class RecentPurchase(
    val amount: String,
    val dosage: String,
    val download_receipt: String,
    val drug_name__c: String,
    val image_url: String,
    val pharmacy: String,
    val purchased_by: String,
    val purchased_on: String,
    val quantity: String
)