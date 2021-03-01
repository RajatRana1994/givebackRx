package com.app.givebackrx.appcode.selectCharityModule

import dagger.Module
import dagger.Provides

@Module
class SelectCharityModule {
    @Provides
    internal fun providesSelectCharityModule(loginDetailPresenter: SelectCharityPresenter<ISelectCharityView>): ISelectCharityPresenter<ISelectCharityView> = loginDetailPresenter
}