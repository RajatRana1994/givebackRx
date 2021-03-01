package com.app.givebackrx.data.entity

data class PharmacyListEntity(
    val auth: Auth,
    val `data`: PharmacyListData,
    val message: String,
    val success: Boolean
)

data class PharmacyListData(
    val best_discount: String,
    val brands: List<PharmacyBrand>,
    val drug_images: List<DrugImages>,
    val drug_info: List<DrugInfo>,
    val custQtyType: String,
    val dosage: String,
    val dosages: List<String>,
    val drugDescription: String,
    var isFavorite: String,
    val drugNdc: String,
    val drug_brand_final_name: String,
    val drug_generic_final_name: String,
    val drug_generic_name: String,
    val drug_name: String,
    var favoriteMedicationId: String,
    val form: String,
    val forms: List<String>,
    val image_url: String,
    val internalDrugPackSize: String,
    val isCustQtyAllowed: String,
    val isDrugGeneric: String,
    val pharmacies: List<PharmacyData>,
    val quantities: List<String>,
    val quantity: String,
    val share_url: String
)

data class PharmacyBrand(
    val label: String,
    val value: String
)
data class DrugImages(
    val drug_image_url: String,
    val drug_image_name: String
)

data class DrugInfo(
    val drug_class: String,
    val drug_availability: String,
    val drug_administration: String,
    val drug_physical_description: String,
    val drug_storage: String,
    val drug_side_effect: String,
    val drug_interaction: String,
    val drug_contra: String,
    val drug_monitor: String,
    val drug_missed: String
)

data class PharmacyData(
    val city: String,
    val distance: String,
    val drugName: String,
    val gold_price: String,
    val is_lowest: String,
    val is_lowest_three: String,
    val latitude: String,
    val longitude: String,
    val marketplace_price: String,
    val nabp: String,
    val ndc: String,
    val npi: String,
    val pbm: String,
    val pharmacy_address_id: String,
    val pharmacy_id: String,
    val pharmacy_name: String,
    val pharmacy_price: String,
    val phone: String,
    val pricebook_entry_id: String,
    val retail_price: String,
    val saving_amount: String,
    val saving_percent: String,
    val silver_price: String,
    val state: String,
    val street_address: String,
    val zip_code: String,
    var visible: Boolean = false
)