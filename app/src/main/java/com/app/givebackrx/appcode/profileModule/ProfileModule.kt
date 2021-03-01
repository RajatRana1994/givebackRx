package com.app.givebackrx.appcode.profileModule

import dagger.Module
import dagger.Provides


@Module
class ProfileModule {
    @Provides
    internal fun providesProfileModule(ProfileDetailPresenter: ProfilePresenter<IProfileView>): IProfilePresenter<IProfileView> =
        ProfileDetailPresenter

}