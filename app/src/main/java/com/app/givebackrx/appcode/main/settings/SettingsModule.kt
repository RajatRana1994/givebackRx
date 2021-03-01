package com.app.givebackrx.appcode.main.settings

import dagger.Module
import dagger.Provides


@Module
class SettingsModule {
    @Provides
    internal fun providesSettingsModule(SettingsDetailPresenter: SettingsPresenter<ISettingsView>): ISettingsPresenter<ISettingsView> =
        SettingsDetailPresenter

}