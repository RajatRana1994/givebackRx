package com.app.givebackrx.data.entity

data class HelpEntity(
    val auth:Auth,
    val `data`: HelpData,
    val message: String,
    val success: Boolean
)

data class HelpData(
    val charity: String,
    val customer: String,
    val faq: List<FaqItem>,
    val partner_in_care: String,
    val pharmacy: String
)

data class FaqItem(
    val answer: String,
    val article_number: String,
    val question: String,
    var visible: Boolean=false
)