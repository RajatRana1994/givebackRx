package com.app.givebackrx.appcode.settings.myNoticesModule

import dagger.Module
import dagger.Provides


@Module
class MyNoticesModule {
    @Provides
    internal fun providesMyNoticesModule(MyNoticesDetailPresenter: MyNoticesPresenter<IMyNoticesView>): IMyNoticesPresenter<IMyNoticesView> =
        MyNoticesDetailPresenter

}