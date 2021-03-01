package com.app.givebackrx.appcode.settings.changePasswordModule

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.NotificationEntity
import com.app.givebackrx.data.entity.SecurityDetailEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity


interface IChangePasswordView : MvpView {
    fun onUpdatedSuccess(it: SuccessMessageEntity)
    fun onSecuritySuccess(it: SecurityDetailEntity)
    fun OnNotificationResp(
        it: SuccessMessageEntity,
        type: Int
    )

    fun OnNotificationGetResp(it: NotificationEntity)
}

interface IChangePasswordPresenter<V : IChangePasswordView> : MvpPresenter<V> {
}