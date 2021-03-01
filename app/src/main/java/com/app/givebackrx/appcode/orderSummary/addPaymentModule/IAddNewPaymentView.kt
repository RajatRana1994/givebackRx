package com.app.givebackrx.appcode.orderSummary.addPaymentModule

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.SuccessMessageEntity


interface IAddNewPaymentView: MvpView {
    fun onAddedCardResp(it: SuccessMessageEntity)
//    fun onAddNewAddressResp(it: AddNewAddressEntity)
//    fun onCardViaEmailed(it: SuccessMessageEntity)
}

interface IAddNewPaymentPresenter<V: IAddNewPaymentView> : MvpPresenter<V> {
}