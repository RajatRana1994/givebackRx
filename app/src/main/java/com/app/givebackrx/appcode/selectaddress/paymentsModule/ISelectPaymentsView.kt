package com.app.givebackrx.appcode.selectaddress.paymentsModule

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.PaymentResEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity


interface ISelectPaymentsView: MvpView {
    fun onAllPaymentRes(it: PaymentResEntity)
    fun onDeleteAddressResp(it: SuccessMessageEntity, pos: Int)
    fun onDefaultAddressResp(it: SuccessMessageEntity, pos: Int)
}

interface ISelectPaymentsPresenter<V: ISelectPaymentsView> : MvpPresenter<V> {
}