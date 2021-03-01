package com.app.givebackrx.appcode.charitysignee

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.CouponGenerationEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity


interface ICharitySigneeView: MvpView {

}

interface ICharitySigneePresenter<V: ICharitySigneeView> : MvpPresenter<V> {
}