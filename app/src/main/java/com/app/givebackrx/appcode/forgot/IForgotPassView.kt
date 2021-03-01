package com.app.givebackrx.appcode.forgot

import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.SignInWithUserDetailData
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity


interface IForgotPassView: MvpView {
    fun onForgotPassResp(it: SignInWithUserDetailEntity)

}