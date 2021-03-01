package com.app.givebackrx.appcode.payment

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.*


interface IPaymentView: MvpView {
    fun goldCardPaymentSummary(it: OrderSummaryModel)
    fun onMembershipResp(it: MemberShipOrderEntity)
    fun goldCardPaymentSummaryError(it: OrderSummaryModel)
    fun onPaymentResp(it: PaymentEntity)
    fun onSavedCardsResp(it: CartEntity)
    fun onDeletedCardResp(it: FavoriteMedicationEntity, position: Int)
}

interface IPaymentPresenter<V: IPaymentView> : MvpPresenter<V> {
}