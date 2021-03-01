package com.app.givebackrx.appcode.settings.nearme

import dagger.Module
import dagger.Provides


@Module
class NearMeModule {
    @Provides
    internal fun providesRefferalModule(RefferalDetailPresenter: NearMePresenter<INearMeView>): INearMePresenter<INearMeView> =
        RefferalDetailPresenter

}