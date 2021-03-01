package com.app.givebackrx.appcode.printedcard

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PrintedCharityProvider{

    @ContributesAndroidInjector(modules = [(PrintedCharityModule::class)])
    abstract fun bindPrintedCharitysActivity(): PrintedCardFragment
}