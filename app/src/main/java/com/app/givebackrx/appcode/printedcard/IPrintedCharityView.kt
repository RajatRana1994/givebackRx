package com.app.givebackrx.appcode.printedcard

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.MyCartEntity
import com.app.givebackrx.data.entity.PrintedCharityEntity


interface IPrintedCharityView: MvpView {
    fun onPrintedCharityResp(it: PrintedCharityEntity)
    fun onAddToCartResp(it: MyCartEntity)
}

interface IPrintedCharityPresenter<V: IPrintedCharityView> : MvpPresenter<V> {
}