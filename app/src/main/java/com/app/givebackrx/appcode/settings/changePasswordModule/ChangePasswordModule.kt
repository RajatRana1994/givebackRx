package com.app.givebackrx.appcode.settings.changePasswordModule

import dagger.Module
import dagger.Provides


@Module
class ChangePasswordModule {
    @Provides
    internal fun providesChangePasswordModule(ChangePasswordDetailPresenter: ChangePasswordPresenter<IChangePasswordView>): IChangePasswordPresenter<IChangePasswordView> =
        ChangePasswordDetailPresenter

}