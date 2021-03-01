package com.app.givebackrx.appcode.orderSummary.thankYouModule

import dagger.Module
import dagger.Provides


@Module
class ThankModule {
    @Provides
    internal fun providesPaymentModule(ThankPresenter: ThankPresenter<IThankView>): IThankPresenter<IThankView> =
            ThankPresenter

}