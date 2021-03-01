package com.app.givebackrx.appcode.charityDetail

import dagger.Module
import dagger.Provides


@Module
class CharityDetailModule {
    @Provides
    internal fun providesCharityDetailModule(loginDetailPresenter: CharityDetailPresenter<ICharityDetailView>): ICharityDetailPresenter<ICharityDetailView> =
        loginDetailPresenter

}