package com.app.givebackrx.data.entity

data class NotificationEntity(
    val auth:Auth,
    val `data`: NotificationData,
    val message: String,
    val success: Boolean
)

data class NotificationData(
    val coupon_experience_satisfaction: Boolean?,
    val givebackrx_news: Boolean?,
    val promotions: Boolean?,
    val user_type: String?
)