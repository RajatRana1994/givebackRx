package com.app.givebackrx.appcode.settings.dashboard

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DashboardProvider{

    @ContributesAndroidInjector(modules = [(DashboardModule::class)])
    abstract fun bindDashboardsActivity(): DashboardFragment
}