package com.app.givebackrx.appcode.settings.purchase

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.RecentPurchaseEntity
import com.app.givebackrx.data.entity.TaskEntity


interface IRecentPurchaseView: MvpView {
    fun onRecentPurchaseResp(it: RecentPurchaseEntity)
}

interface IRecentPurchasePresenter<V: IRecentPurchaseView> : MvpPresenter<V> {
}