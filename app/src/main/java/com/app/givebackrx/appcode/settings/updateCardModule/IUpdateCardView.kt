package com.app.givebackrx.appcode.settings.updateCardModule

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.*


interface IUpdateCardView: MvpView {
    fun onDashboardResp(it: DashboardEntity)
    fun onMembershipResp(it: MemberShipOrderEntity)
    fun onCardViaEmailed(it: SuccessMessageEntity)
    fun onSavedCardsResp(it: CartEntity)
    fun onDeletedCardResp(it: FavoriteMedicationEntity, position: Int)
    fun onUpdateCardResp(it: SuccessMessageEntity)
    fun onDisableRecurringPaymentResp(it: SuccessMessageEntity)
}

interface IUpdateCardPresenter<V: IUpdateCardView> : MvpPresenter<V> {
}