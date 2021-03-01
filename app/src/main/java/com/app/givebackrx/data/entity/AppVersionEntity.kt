package com.app.givebackrx.data.entity

data class AppVersionEntity(
    val auth: Auth,
    val `data`: AppVersionData,
    val message: String,
    val success: Boolean
)

data class AppVersionData(
    var version: String
)