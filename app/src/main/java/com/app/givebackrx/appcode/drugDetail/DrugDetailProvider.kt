package com.app.givebackrx.appcode.drugDetail

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DrugDetailProvider{

    @ContributesAndroidInjector(modules = [(DrugDetailModule::class)])
    abstract fun bindDrugDetailsActivity(): DrugDetailFragment
}