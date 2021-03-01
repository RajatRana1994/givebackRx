package com.app.givebackrx.appcode.orderSummary.shippingModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ShipmentProvider{

    @ContributesAndroidInjector(modules = [(ShipmentModule::class)])
    abstract fun bindShippingActivity(): ShippingFragment
}