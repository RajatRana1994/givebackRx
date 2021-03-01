package com.app.givebackrx.appcode.orderSummary.billingModule

import dagger.Module
import dagger.Provides


@Module
class BillingModule {
    @Provides
    internal fun providesPaymentModule(BillingPresenter: BillingPresenter<IBillingView>): IBillingPresenter<IBillingView> =
            BillingPresenter

}