package com.app.givebackrx.appcode.main.home

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.AppVersionEntity
import com.app.givebackrx.data.entity.SearchDrugEntity
import com.google.gson.JsonObject


interface IHomeView: MvpView {
    fun onDrugSearched(it: SearchDrugEntity)
    fun onAppVersionResp(it: AppVersionEntity)
//    fun onHomesResponse(it: AllHomesEntity)
}

interface IHomePresenter<V: IHomeView> : MvpPresenter<V> {
//    fun getHome(jsonObject: JsonObject)
}