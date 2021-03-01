package com.app.givebackrx.appcode.selectaddress.paymentsModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SelectPaymentProvider{

    @ContributesAndroidInjector(modules = [(SelectPaymentModule::class)])
    abstract fun bindSelectPaymentsActivity(): SelectPaymentsFragment
}