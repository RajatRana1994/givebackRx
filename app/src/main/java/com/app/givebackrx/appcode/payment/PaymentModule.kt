package com.app.givebackrx.appcode.payment

import dagger.Module
import dagger.Provides


@Module
class PaymentModule {
    @Provides
    internal fun providesPaymentModule(PaymentDetailPresenter: PaymentPresenter<IPaymentView>): IPaymentPresenter<IPaymentView> =
        PaymentDetailPresenter

}