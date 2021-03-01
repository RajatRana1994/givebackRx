package com.app.givebackrx.appcode.charitysignee

import dagger.Module
import dagger.Provides


@Module
class CharitySigneeModule {
    @Provides
    internal fun providesCharitySigneeModule(CharitySigneeDetailPresenter: CharitySigneePresenter<ICharitySigneeView>): ICharitySigneePresenter<ICharitySigneeView> =
        CharitySigneeDetailPresenter

}