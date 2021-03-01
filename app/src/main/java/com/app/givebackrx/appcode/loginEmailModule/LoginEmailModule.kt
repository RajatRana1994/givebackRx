package com.app.givebackrx.appcode.loginEmailModule

import dagger.Module
import dagger.Provides


@Module
class LoginEmailModule {
    @Provides
    internal fun providesLoginEmailModule(loginDetailPresenter: LoginEmailPresenter<ILoginEmailView>): ILoginEmailPresenter<ILoginEmailView> =
        loginDetailPresenter

}