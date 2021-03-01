package com.app.givebackrx.appcode.preSignUpModule

import dagger.Module
import dagger.Provides


@Module
class PreSignupCheckModule {
    @Provides
    internal fun providesPreSignupCheckModule(loginDetailPresenter: PreSignupCheckPresenter<IPreSignupCheckView>): IPreSignupCheckPresenter<IPreSignupCheckView> =
        loginDetailPresenter

}