package com.app.givebackrx.appcode.selectCharityModule

import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.CharityListEntity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity


interface ISelectCharityView: MvpView {
    fun onGotCharityList(it: CharityListEntity)
    fun onSignupResponse(it: SignInWithUserDetailEntity)
//    fun onPreSignUpVerification(canRegister: Boolean)

}