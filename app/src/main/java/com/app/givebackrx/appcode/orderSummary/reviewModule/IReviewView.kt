package com.app.givebackrx.appcode.orderSummary.reviewModule

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.*


interface IReviewView: MvpView {
    fun onOrderStoreSummary(it: StoreOrderSummary)
    fun onOrderStorePayment(it: SuccessMessageEntity)
    fun onOrderStorePaymentHold(it: HoldResponseEntity)
}

interface IReviewPresenter<V: IReviewView> : MvpPresenter<V> {
}