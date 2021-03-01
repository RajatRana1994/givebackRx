package com.app.givebackrx.appcode.settings.nearme

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.DashboardEntity
import com.app.givebackrx.data.entity.NearMeEntity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity


interface INearMeView: MvpView {
    fun onNearMeResp(it: NearMeEntity)
}

interface INearMePresenter<V: INearMeView> : MvpPresenter<V> {
}