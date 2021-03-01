package com.app.givebackrx.appcode.selectaddress.addnewaddress

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AddNewAddressProvider{

    @ContributesAndroidInjector(modules = [(AddNewAddressModule::class)])
    abstract fun bindAddNewAddresssActivity(): AddNewAddressFragment
}