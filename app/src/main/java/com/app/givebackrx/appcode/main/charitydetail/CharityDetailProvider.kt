package com.app.givebackrx.appcode.main.charitydetail

import com.app.givebackrx.appcode.charityDetail.CharityDetailModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CharityDetailProvider{

    @ContributesAndroidInjector(modules = [(CharityDetailModule::class)])
    abstract fun bindCharityDetailFragment(): CharityDetailFragment
}