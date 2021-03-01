package com.app.givebackrx.appcode.settings.referralsModule

import dagger.Module
import dagger.Provides


@Module
class ReferralModule {
    @Provides
    internal fun providesRefferalModule(RefferalDetailPresenter: ReferralPresenter<IReferralView>): IReferralPresenter<IReferralView> =
        RefferalDetailPresenter

}