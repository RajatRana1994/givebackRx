package com.app.givebackrx.appcode.selectaddress

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.AddressesListEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity


interface ISelectAddressView: MvpView {
    fun onAllAddressResp(it: AddressesListEntity)
    fun onDeleteAddressResp(
        it: SuccessMessageEntity,
        pos: Int
    )

    fun onDefaultAddressResp(it: SuccessMessageEntity, pos: Int)
//    fun onSelectAddressResp(it: SelectAddressEntity)
//    fun onCardViaEmailed(it: SuccessMessageEntity)
}

interface ISelectAddressPresenter<V: ISelectAddressView> : MvpPresenter<V> {
}