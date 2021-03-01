package com.app.givebackrx.appcode.orderSummary.thankYouModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ThankProvider{

    @ContributesAndroidInjector(modules = [(ThankModule::class)])
    abstract fun bindThankYouActivity(): ThankYouFragment
}