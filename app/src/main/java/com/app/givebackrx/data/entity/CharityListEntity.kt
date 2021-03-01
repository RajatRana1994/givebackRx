package com.app.givebackrx.data.entity

data class CharityListEntity(
    val auth:Auth,
    val count: Int,
    val `data`: CharityListData,
    val message: String,
    val success: Boolean
)

data class CharityListData(
    val categories: List<String>,
    val charities: List<CharityData>,
    val cities: List<String>,
    val current_page: Int?=0,
    val pages: List<Int>,
    val sort_by_alphabet: List<String>,
    val states: List<String>
)

data class CharityData(
    val charity_id: String,
    val charity_logo: String,
    val charity_name: String,
    val description: String,
    val donation_from_giveback_enterprises: String,
    var selected: Boolean
)