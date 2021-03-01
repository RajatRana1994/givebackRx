package com.app.givebackrx.appcode.orderSummary.thankYouModule

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.*


interface IThankView: MvpView {
}

interface IThankPresenter<V: IThankView> : MvpPresenter<V> {
}