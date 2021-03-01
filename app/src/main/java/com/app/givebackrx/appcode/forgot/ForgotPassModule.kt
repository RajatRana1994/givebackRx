package com.app.givebackrx.appcode.forgot

import dagger.Module
import dagger.Provides


@Module
class ForgotPassModule {
    @Provides
    internal fun providesForgotPassModule(loginDetailPresenter: ForgotPassPresenter<IForgotPassView>): IForgotPassPresenter<IForgotPassView> =
        loginDetailPresenter

}