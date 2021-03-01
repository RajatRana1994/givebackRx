package com.app.givebackrx.appcode.settings.favouriteModule

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.FavoriteMedicationEntity


interface IFavouriteView: MvpView {
    fun onFavoriteMedicationResp(it: FavoriteMedicationEntity)
    fun onDeleteFavoriteMedicationResp(
        it: FavoriteMedicationEntity,
        position: Int
    )
}

interface IReferralPresenter<V: IFavouriteView> : MvpPresenter<V> {
}