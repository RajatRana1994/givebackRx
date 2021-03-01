package com.app.givebackrx.appcode.settings.help

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HelpProvider{

    @ContributesAndroidInjector(modules = [(HelpModule::class)])
    abstract fun bindHelpsActivity(): HelpFragment

   @ContributesAndroidInjector(modules = [(HelpModule::class)])
    abstract fun bindHelpTicketFragment(): HelpTicketFragment
}