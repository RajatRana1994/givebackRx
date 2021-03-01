package com.app.givebackrx.appcode.storecheckout

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.FavoriteMedicationEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity


interface IStoreCheckoutView: MvpView {
    fun onCartPaymentResp(it: SuccessMessageEntity)
    fun onDeletedCardResp(it: FavoriteMedicationEntity, position: Int)
//    fun onStoreCheckoutResp(it: StoreCheckoutEntity)
//    fun onCardViaEmailed(it: SuccessMessageEntity)
}

interface IStoreCheckoutPresenter<V: IStoreCheckoutView> : MvpPresenter<V> {
}