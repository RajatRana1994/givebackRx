package com.app.givebackrx.appcode.settings.myTasksModule

import com.app.givebackrx.appcode.main.settings.myTasksModule.MyTaskFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MyTaskProvider{

    @ContributesAndroidInjector(modules = [(MyTaskModule::class)])
    abstract fun bindMyTasksActivity(): MyTaskFragment
}