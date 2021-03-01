package com.app.givebackrx.appcode.storedetail

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.*


interface IStoreDetailView: MvpView {
    fun onStoreDetailResp(it: StoreDetailEntity)
    fun onAddToCartResp(it: MyCartEntity)
//    fun onStoreDetailResp(it: StoreListEntity)
}

interface IStoreDetailPresenter<V: IStoreDetailView> : MvpPresenter<V> {
}