package com.app.givebackrx.appcode.mycharitydetail

import dagger.Module
import dagger.Provides


@Module
class MyCharityDetailModule {
    @Provides
    internal fun providesMyCharityDetailModule(loginDetailPresenter: MyCharityDetailPresenter<IMyCharityDetailView>): IMyCharityDetailPresenter<IMyCharityDetailView> =
        loginDetailPresenter

}