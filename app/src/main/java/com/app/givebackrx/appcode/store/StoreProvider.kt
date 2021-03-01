package com.app.givebackrx.appcode.store

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class StoreProvider{

    @ContributesAndroidInjector(modules = [(StoreModule::class)])
    abstract fun bindStoreFragment(): StoreFragment
}