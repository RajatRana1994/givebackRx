package com.app.givebackrx.appcode.main.pharmacy

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PharmacyProvider{

    @ContributesAndroidInjector(modules = [(PharmacyModule::class)])
    abstract fun bindDrugDetailsActivity(): PharmacyFragment
}