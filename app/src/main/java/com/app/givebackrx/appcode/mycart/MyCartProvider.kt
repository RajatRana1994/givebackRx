package com.app.givebackrx.appcode.mycart

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MyCartProvider{

    @ContributesAndroidInjector(modules = [(MyCartModule::class)])
    abstract fun bindStoreFragment(): MyCartFragment
}