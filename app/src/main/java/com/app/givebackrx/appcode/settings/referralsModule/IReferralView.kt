package com.app.givebackrx.appcode.settings.referralsModule

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.DashboardEntity
import com.app.givebackrx.data.entity.ReferralEntity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity


interface IReferralView: MvpView {
    fun onReferralResp(it: ReferralEntity)
    fun onInvitedReferral(it: SignInWithUserDetailEntity)
    fun onResendReferralResp(it: DashboardEntity)
}

interface IReferralPresenter<V: IReferralView> : MvpPresenter<V> {
}