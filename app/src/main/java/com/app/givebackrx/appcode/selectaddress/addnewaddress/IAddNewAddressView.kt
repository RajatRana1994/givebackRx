package com.app.givebackrx.appcode.selectaddress.addnewaddress

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.data.entity.SuccessModelResponse


interface IAddNewAddressView: MvpView {
    fun onAddedAddressResp(it: SuccessMessageEntity)
//    fun onAddNewAddressResp(it: AddNewAddressEntity)
//    fun onCardViaEmailed(it: SuccessMessageEntity)
}

interface IAddNewAddressPresenter<V: IAddNewAddressView> : MvpPresenter<V> {
}