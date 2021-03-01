package com.app.givebackrx.appcode.charityDetail

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.Auth
import com.app.givebackrx.data.entity.CharityDetailData
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity


interface ICharityDetailView: MvpView {
    fun onSignupResponse(it: SignInWithUserDetailEntity)
    fun onCharityDetailResponse(
        data: CharityDetailData,
        auth: Auth
    )
    fun onSelectedCharity(it: SuccessMessageEntity)
    fun onCardViaEmailed(it: SuccessMessageEntity)

}


interface ICharityDetailPresenter<V: ICharityDetailView> : MvpPresenter<V> {
//    fun updateProfile(jsonObject: JsonObject)
//    fun employee_details(jsonObject: JsonObject)

}