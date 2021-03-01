package com.app.givebackrx.appcode.selectaddress

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SelectAddressProvider{

    @ContributesAndroidInjector(modules = [(SelectAddressModule::class)])
    abstract fun bindSelectAddresssActivity(): SelectAddressFragment
}