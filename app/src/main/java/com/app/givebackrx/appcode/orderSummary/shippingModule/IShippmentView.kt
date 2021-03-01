package com.app.givebackrx.appcode.orderSummary.shippingModule

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.*


interface IShippmentView: MvpView {
    fun onOrderStoreSummary(it: StoreOrderSummary)
}

interface IShipmentPresenter<V: IShippmentView> : MvpPresenter<V> {
}