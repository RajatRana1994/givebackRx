package com.app.givebackrx.appcode.loginEmailPaswordModule

import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.SignInWithUserDetailData


interface ILoginEmailPasswordView: MvpView {
    fun onSignInWithUserDetail(data: SignInWithUserDetailData)

}