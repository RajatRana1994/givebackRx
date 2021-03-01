package com.app.givebackrx.appcode.preSignUpModule

import com.app.givebackrx.base.MvpView


interface IPreSignupCheckView: MvpView {
    fun onPreSignUpVerification(canRegister: Boolean)

}