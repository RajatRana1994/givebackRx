package com.app.givebackrx.appcode.main.charity

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.CharityListEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity


interface ICharityView: MvpView {
    fun onGotCharityList(it: CharityListEntity)
    fun onSelectedCharity(it: SuccessMessageEntity)
//    fun onCharitysResponse(it: AllCharitysEntity)
}

interface ICharityPresenter<V: ICharityView> : MvpPresenter<V> {
//    fun getCharity(jsonObject: JsonObject)
}