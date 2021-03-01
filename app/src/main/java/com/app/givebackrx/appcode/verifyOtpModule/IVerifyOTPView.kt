package com.app.givebackrx.appcode.verifyOtpModule

import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.SignInWithUserDetailData


interface IVerifyOTPView: MvpView {
    abstract fun onVerifyOTPResponse(it: SignInWithUserDetailData)

}