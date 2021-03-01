package com.app.givebackrx.appcode.signupModule

import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity


interface ISignupView: MvpView {
    fun onPostSignUpVerification(it: SuccessMessageEntity)
    fun onSignupResponse(it: SignInWithUserDetailEntity)
//    fun onPreSignUpVerification(canRegister: Boolean)

}