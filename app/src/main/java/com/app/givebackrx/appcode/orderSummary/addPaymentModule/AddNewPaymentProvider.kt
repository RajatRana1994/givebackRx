package com.app.givebackrx.appcode.orderSummary.addPaymentModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AddNewPaymentProvider{

    @ContributesAndroidInjector(modules = [(AddNewPaymentModule::class)])
    abstract fun bindAddNewPaymentActivity(): AddNewPaymentFragment
}