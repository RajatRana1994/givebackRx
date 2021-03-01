package com.app.givebackrx.appcode.settings.dashboard

import dagger.Module
import dagger.Provides


@Module
class DashboardModule {
    @Provides
    internal fun providesDashboardModule(DashboardDetailPresenter: DashboardPresenter<IDashboardView>): IDashboardPresenter<IDashboardView> =
        DashboardDetailPresenter

}