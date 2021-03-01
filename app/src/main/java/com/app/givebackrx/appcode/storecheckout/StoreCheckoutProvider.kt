package com.app.givebackrx.appcode.storecheckout

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class StoreCheckoutProvider{

    @ContributesAndroidInjector(modules = [(StoreCheckoutModule::class)])
    abstract fun bindStoreCheckoutsActivity(): StoreCheckoutFragment
}