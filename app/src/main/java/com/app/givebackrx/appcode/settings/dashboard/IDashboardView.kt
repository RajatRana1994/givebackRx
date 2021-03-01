package com.app.givebackrx.appcode.settings.dashboard

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.CouponGenerationEntity
import com.app.givebackrx.data.entity.DashboardEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity


interface IDashboardView: MvpView {
    fun onDashboardResp(it: DashboardEntity)
    fun onCardViaEmailed(it: SuccessMessageEntity)
}

interface IDashboardPresenter<V: IDashboardView> : MvpPresenter<V> {
}