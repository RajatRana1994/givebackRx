package com.app.givebackrx.appcode.settings.favouriteModule

import dagger.Module
import dagger.Provides


@Module
class FavouriteModule {
    @Provides
    internal fun providesFavouriteModule(RefferalDetailPresenter: FavouritePresenter<IFavouriteView>): IReferralPresenter<IFavouriteView> =
        RefferalDetailPresenter

}