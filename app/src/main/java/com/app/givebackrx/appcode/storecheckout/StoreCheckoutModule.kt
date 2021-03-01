package com.app.givebackrx.appcode.storecheckout

import dagger.Module
import dagger.Provides


@Module
class StoreCheckoutModule {
    @Provides
    internal fun providesStoreCheckoutModule(StoreCheckoutDetailPresenter: StoreCheckoutPresenter<IStoreCheckoutView>): IStoreCheckoutPresenter<IStoreCheckoutView> =
        StoreCheckoutDetailPresenter

}