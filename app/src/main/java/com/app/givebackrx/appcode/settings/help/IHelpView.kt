package com.app.givebackrx.appcode.settings.help

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.HelpEntity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity


interface IHelpView: MvpView {
    fun onHelpResp(it: HelpEntity)
    fun onHelpTicketResp(it: SignInWithUserDetailEntity)
}

interface IHelpPresenter<V: IHelpView> : MvpPresenter<V> {
}