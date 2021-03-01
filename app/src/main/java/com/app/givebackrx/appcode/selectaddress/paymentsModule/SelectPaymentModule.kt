package com.app.givebackrx.appcode.selectaddress.paymentsModule

import dagger.Module
import dagger.Provides


@Module
class SelectPaymentModule {
    @Provides
    internal fun providesSelectAddressModule(SelectPaymentPresenter: SelectPaymentPresenter<ISelectPaymentsView>): ISelectPaymentsPresenter<ISelectPaymentsView> =
            SelectPaymentPresenter

}