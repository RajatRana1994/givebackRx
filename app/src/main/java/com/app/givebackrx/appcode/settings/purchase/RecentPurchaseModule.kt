package com.app.givebackrx.appcode.settings.purchase

import dagger.Module
import dagger.Provides


@Module
class RecentPurchaseModule {
    @Provides
    internal fun providesRecentPurchaseModule(RecentPurchaseDetailPresenter: RecentPurchasePresenter<IRecentPurchaseView>): IRecentPurchasePresenter<IRecentPurchaseView> =
        RecentPurchaseDetailPresenter

}