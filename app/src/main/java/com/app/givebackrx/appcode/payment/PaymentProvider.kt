package com.app.givebackrx.appcode.payment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PaymentProvider{

    @ContributesAndroidInjector(modules = [(PaymentModule::class)])
    abstract fun bindPaymentsActivity(): PaymentFragment
}