package com.app.givebackrx.appcode.main.charity

import dagger.Module
import dagger.Provides


@Module
class CharityModule {
    @Provides
    internal fun providesCharityModule(CharityDetailPresenter: CharityPresenter<ICharityView>): ICharityPresenter<ICharityView> =
        CharityDetailPresenter

}