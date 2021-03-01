package com.app.givebackrx.data.network

interface ApiHelper {
    fun restService(): RestService
    fun authdetailService(): RestService
    fun distanceService(): RestService

}