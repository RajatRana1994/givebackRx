package com.app.givebackrx.appcode.settings.mycharity

import dagger.Module
import dagger.Provides


@Module
class MyCharityModule {
    @Provides
    internal fun providesMyCharityModule(MyCharityDetailPresenter: MyCharityPresenter<IMyCharityView>): IMyCharityPresenter<IMyCharityView> =
        MyCharityDetailPresenter

}