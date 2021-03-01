package com.app.givebackrx.appcode.drugDetail

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.CouponGenerationEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity


interface IDrugDetailView: MvpView {
    fun onCouponGenerated(it: CouponGenerationEntity)
    fun onCouponViaEmailed(it: SuccessMessageEntity)
    fun onAddedFavResp(it: SuccessMessageEntity)
}

interface IDrugDetailPresenter<V: IDrugDetailView> : MvpPresenter<V> {
}