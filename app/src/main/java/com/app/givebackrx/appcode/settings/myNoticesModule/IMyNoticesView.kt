package com.app.givebackrx.appcode.settings.myNoticesModule

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.NoticeEntity


interface IMyNoticesView: MvpView {
    fun onNoticeResp(it: NoticeEntity)
    fun onNoticeAcknowledged(it: NoticeEntity, position: Int)
}

interface IMyNoticesPresenter<V: IMyNoticesView> : MvpPresenter<V> {
}