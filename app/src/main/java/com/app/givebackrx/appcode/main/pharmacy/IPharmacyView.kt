package com.app.givebackrx.appcode.main.pharmacy

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.Auth
import com.app.givebackrx.data.entity.FavoriteMedicationEntity
import com.app.givebackrx.data.entity.PharmacyListData
import com.app.givebackrx.data.entity.SuccessMessageEntity


interface IPharmacyView: MvpView {
    fun onPharmacyResponse(
        it: PharmacyListData,
        auth: Auth
    )
    fun onAddedFavResp(it: SuccessMessageEntity)
    fun onDeleteFavoriteMedicationResp(it: FavoriteMedicationEntity)
}

interface IPharmacyPresenter<V: IPharmacyView> : MvpPresenter<V> {
}