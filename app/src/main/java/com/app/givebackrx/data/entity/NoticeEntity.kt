package com.app.givebackrx.data.entity

data class NoticeEntity(
    val auth:Auth,
    val count: Int,
    val `data`: NoticeData,
    val message: String,
    val success: Boolean
)

data class NoticeData(
    val my_notices: List<MyNoticeItem>
)

data class MyNoticeItem(
    val created_date: String,
    val description: String,
    val notice_id: String,
    val notice_name: String,
    var notice_status: String
)