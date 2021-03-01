package com.app.givebackrx.appcode.mycart

import dagger.Module
import dagger.Provides


@Module
class MyCartModule {
    @Provides
    internal fun providesMyCartModule(MyCartPresenter: MyCartPresenter<IMyCartView>): IMyCartPresenter<IMyCartView> =
        MyCartPresenter

}