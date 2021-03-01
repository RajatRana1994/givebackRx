package com.app.givebackrx.appcode.store

import dagger.Module
import dagger.Provides


@Module
class StoreModule {
    @Provides
    internal fun providesStoreModule(StoreDetailPresenter: StorePresenter<IStoreView>): IStorePresenter<IStoreView> =
        StoreDetailPresenter

}