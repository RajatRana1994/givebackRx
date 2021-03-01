package com.app.givebackrx.appcode.settings.purchase

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RecentPurchaseProvider{

    @ContributesAndroidInjector(modules = [(RecentPurchaseModule::class)])
    abstract fun bindRecentPurchasesActivity(): RecentPurchaseFragment
}