package com.app.givebackrx.appcode.storedetail

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class StoreDetailProvider{

    @ContributesAndroidInjector(modules = [(StoreDetailModule::class)])
    abstract fun bindStoreFragment(): StoreDetailFragment
}