package com.app.givebackrx.appcode.main.settings

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsProvider{

    @ContributesAndroidInjector(modules = [(SettingsModule::class)])
    abstract fun bindSettingssActivity(): SettingsFragment
}