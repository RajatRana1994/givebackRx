package com.app.givebackrx.appcode.main.settings

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.google.gson.JsonObject


interface ISettingsView: MvpView {
//    fun onSettingssResponse(it: AllSettingssEntity)
}

interface ISettingsPresenter<V: ISettingsView> : MvpPresenter<V> {
//    fun getSettings(jsonObject: JsonObject)
}