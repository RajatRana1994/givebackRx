package com.app.givebackrx.appcode.settings.updateCardModule

import dagger.Module
import dagger.Provides


@Module
class UpdateCardModule {
    @Provides
    internal fun providesUpdateCardModule(UpdateCardDetailPresenter: UpdateCardPresenter<IUpdateCardView>): IUpdateCardPresenter<IUpdateCardView> =
        UpdateCardDetailPresenter

}