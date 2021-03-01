package com.app.givebackrx.appcode.settings.myNoticesModule

import com.app.givebackrx.appcode.main.settings.myNoticesModule.MyNoticesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MyNoticesProvider{

    @ContributesAndroidInjector(modules = [(MyNoticesModule::class)])
    abstract fun bindMyNoticessActivity(): MyNoticesFragment
}