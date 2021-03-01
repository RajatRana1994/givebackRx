package com.app.givebackrx.appcode.settings.nearme

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class NearMeProvider{

    @ContributesAndroidInjector(modules = [(NearMeModule::class)])
    abstract fun bindNearMeFragment(): NearMeFragment
}