package com.app.givebackrx.appcode.loginEmailPaswordModule

import dagger.Module
import dagger.Provides


@Module
class LoginEmailPasswordModule {
    @Provides
    internal fun providesLoginEmailPasswordModule(loginDetailPresenter: LoginEmailPasswordPresenter<ILoginEmailPasswordView>): ILoginEmailPasswordPresenter<ILoginEmailPasswordView> =
        loginDetailPresenter

}