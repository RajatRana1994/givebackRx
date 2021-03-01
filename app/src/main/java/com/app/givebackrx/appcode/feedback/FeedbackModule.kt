package com.app.givebackrx.appcode.settings.FeedbackModule

import dagger.Module
import dagger.Provides


@Module
class FeedbackModule {
    @Provides
    internal fun providesFeedbackModule(RefferalDetailPresenter: FeedbackPresenter<IFeedbackView>): IReferralPresenter<IFeedbackView> =
        RefferalDetailPresenter

}