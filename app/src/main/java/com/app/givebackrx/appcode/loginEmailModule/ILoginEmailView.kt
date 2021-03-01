package com.app.givebackrx.appcode.loginEmailModule

import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.SignInWithUserDetailData


interface ILoginEmailView: MvpView {
    fun onSignInWithUserDetail(data: SignInWithUserDetailData)

}