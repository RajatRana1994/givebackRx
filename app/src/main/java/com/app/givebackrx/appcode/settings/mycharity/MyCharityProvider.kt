package com.app.givebackrx.appcode.settings.mycharity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MyCharityProvider{

    @ContributesAndroidInjector(modules = [(MyCharityModule::class)])
    abstract fun bindMyCharityActivity(): MyCharityFragment
}