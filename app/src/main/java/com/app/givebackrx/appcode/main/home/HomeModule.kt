package com.app.givebackrx.appcode.main.home

import dagger.Module
import dagger.Provides


@Module
class HomeModule {
    @Provides
    internal fun providesHomeModule(HomeDetailPresenter: HomePresenter<IHomeView>): IHomePresenter<IHomeView> =
        HomeDetailPresenter

}