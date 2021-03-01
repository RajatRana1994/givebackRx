package com.app.givebackrx.appcode.settings.help

import dagger.Module
import dagger.Provides


@Module
class HelpModule {
    @Provides
    internal fun providesHelpModule(HelpDetailPresenter: HelpPresenter<IHelpView>): IHelpPresenter<IHelpView> =
        HelpDetailPresenter

}