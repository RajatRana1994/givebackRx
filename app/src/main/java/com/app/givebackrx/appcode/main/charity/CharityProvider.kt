package com.app.givebackrx.appcode.main.charity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CharityProvider{

    @ContributesAndroidInjector(modules = [(CharityModule::class)])
    abstract fun bindCharitysActivity(): CharityFragment
}