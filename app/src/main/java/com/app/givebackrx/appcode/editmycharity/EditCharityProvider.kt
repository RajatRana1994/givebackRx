package com.app.givebackrx.appcode.editmycharity

import com.app.givebackrx.appcode.settings.mycharity.MyCharityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class EditCharityProvider{

    @ContributesAndroidInjector(modules = [(MyCharityModule::class)])
    abstract fun bindMyCharityDetailFragment(): EditMyCharityFragment
}