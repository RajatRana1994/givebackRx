package com.app.givebackrx.data.entity

data class FavoriteMedicationEntity(
    val auth: Auth,
    val count: Int,
    val `data`: FavoriteMedicationData,
    val message: String,
    val success: Boolean
)

data class FavoriteMedicationData(
    val favorite_medication: List<FavoriteMedication>
)

data class FavoriteMedication(
    val dosage: String,
    val drug_generic_name: String,
    val drug_name__c: String,
    val favorite_medication_id: String,
    val image_url: String,
    val pharmacy_address_id: String,
    val pricebook_entry_id: String,
    val pricing: Pricing,
    val quantity: String
)

data class Pricing(
    val gold_price: String,
    val marketplace_price: String,
    val silver_price: String
)