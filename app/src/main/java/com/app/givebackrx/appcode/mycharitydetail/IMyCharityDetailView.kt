package com.app.givebackrx.appcode.mycharitydetail

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.*


interface IMyCharityDetailView: MvpView {
    fun onMyCharityDetailResponse(data: MyCharityDetailEntity)
    fun onCardViaEmailed(it: SuccessMessageEntity)

}


interface IMyCharityDetailPresenter<V: IMyCharityDetailView> : MvpPresenter<V> {


}