package com.app.givebackrx.appcode.storedetail

import dagger.Module
import dagger.Provides


@Module
class StoreDetailModule {
    @Provides
    internal fun providesStoreDetailModule(StoreDetailPresenter: StoreDetailPresenter<IStoreDetailView>): IStoreDetailPresenter<IStoreDetailView> =
        StoreDetailPresenter

}