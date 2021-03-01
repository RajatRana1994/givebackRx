package com.app.givebackrx.appcode.orderSummary.billingModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BillingProvider{

    @ContributesAndroidInjector(modules = [(BillingModule::class)])
    abstract fun bindPaymentsActivity(): BillingFragment
}