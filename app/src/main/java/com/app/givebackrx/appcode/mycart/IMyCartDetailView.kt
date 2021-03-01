package com.app.givebackrx.appcode.mycart

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.*


interface IMyCartView: MvpView {
    fun onMyCartResp(it: CartEntity)
    fun onUpdateToCartResp(
        it: MyCartEntity,
        action: String,
        pos: Int
    )
//    fun onStoreDetailResp(it: StoreDetailEntity)
//    fun onStoreDetailResp(it: StoreListEntity)
}

interface IMyCartPresenter<V: IMyCartView> : MvpPresenter<V> {
}