package com.app.givebackrx.appcode.orderSummary.addPaymentModule

import dagger.Module
import dagger.Provides


@Module
class AddNewPaymentModule {
    @Provides
    internal fun providesSelectAddressModule(SelectPaymentDetailPresenter: AddNewPaymentPresenter<IAddNewPaymentView>): IAddNewPaymentPresenter<IAddNewPaymentView> =
        SelectPaymentDetailPresenter

}