package com.app.givebackrx.appcode.settings.changePasswordModule

import com.app.givebackrx.appcode.settings.notificationModule.NotificationsFragment
import com.app.givebackrx.appcode.settings.securityModule.SecurityFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ChangePasswordProvider{

    @ContributesAndroidInjector(modules = [(ChangePasswordModule::class)])
    abstract fun bindChangePasswordsActivity(): ChangePasswordFragment

    @ContributesAndroidInjector(modules = [(ChangePasswordModule::class)])
    abstract fun bindSecurityFragment(): SecurityFragment

    @ContributesAndroidInjector(modules = [(ChangePasswordModule::class)])
    abstract fun bindNotificationsFragment(): NotificationsFragment
}