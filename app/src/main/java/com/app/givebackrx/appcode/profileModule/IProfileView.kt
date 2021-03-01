package com.app.givebackrx.appcode.profileModule

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.ProfileEntity
import com.app.givebackrx.data.entity.ProfileUpdatedEntity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.entity.VerifySecondaryOtpEntity


interface IProfileView: MvpView {
    fun onProfileResponse(it: ProfileEntity)
    fun onProfileImageResponse(it: ProfileUpdatedEntity)
    fun onUpdateSecondaryResp(
        it: VerifySecondaryOtpEntity,
        value: String,
        type: String
    )
    fun onVerifySecondaryOTPResp(
        it: SignInWithUserDetailEntity,
        email: String,
        type: String
    )

    fun onEditProfileResponse(it: VerifySecondaryOtpEntity)
}

interface IProfilePresenter<V: IProfileView> : MvpPresenter<V> {
}