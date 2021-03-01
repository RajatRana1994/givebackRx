package com.app.givebackrx.appcode.orderSummary.billingModule

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.*


interface IBillingView: MvpView {
    fun onOrderStoreSummary(it: StoreOrderSummary)
}

interface IBillingPresenter<V: IBillingView> : MvpPresenter<V> {
}