package com.app.givebackrx.appcode.store

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.CouponGenerationEntity
import com.app.givebackrx.data.entity.DashboardEntity
import com.app.givebackrx.data.entity.StoreListEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity


interface IStoreView: MvpView {
    fun onStoreListResp(it: StoreListEntity)
}

interface IStorePresenter<V: IStoreView> : MvpPresenter<V> {
}