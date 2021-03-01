package com.app.givebackrx.appcode.profileModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProfileProvider{

    @ContributesAndroidInjector(modules = [(ProfileModule::class)])
    abstract fun bindProfilesActivity(): ProfileFragment
}