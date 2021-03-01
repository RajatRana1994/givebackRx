package com.app.givebackrx.appcode.settings.referralsModule

import com.app.givebackrx.appcode.main.settings.referralsModule.ReferralsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ReferralProvider{

    @ContributesAndroidInjector(modules = [(ReferralModule::class)])
    abstract fun bindRefferalsActivity(): ReferralsFragment
}