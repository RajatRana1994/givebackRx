package com.app.givebackrx.appcode.mycharitydetail

import com.app.givebackrx.appcode.settings.mycharity.MyCharityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MyCharityDetailProvider{

    @ContributesAndroidInjector(modules = [(MyCharityModule::class)])
    abstract fun bindMyCharityDetailFragment(): MyCharityDetailFragment
}