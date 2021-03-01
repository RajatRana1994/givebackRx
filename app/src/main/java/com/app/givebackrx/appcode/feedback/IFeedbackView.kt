package com.app.givebackrx.appcode.settings.FeedbackModule

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.FavoriteMedicationEntity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity


interface IFeedbackView: MvpView {
    abstract fun onFeedbackResp(it: SignInWithUserDetailEntity)
}

interface IReferralPresenter<V: IFeedbackView> : MvpPresenter<V> {
}