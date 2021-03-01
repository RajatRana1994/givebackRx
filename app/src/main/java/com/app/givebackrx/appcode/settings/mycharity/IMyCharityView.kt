package com.app.givebackrx.appcode.settings.mycharity

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.MyCharityEntity
import com.app.givebackrx.data.entity.NoticeEntity


interface IMyCharityView: MvpView {
    fun onMyCharityResp(it: MyCharityEntity)
}

interface IMyCharityPresenter<V: IMyCharityView> : MvpPresenter<V> {
}