package com.app.givebackrx.appcode.orderSummary.reviewModule

import dagger.Module
import dagger.Provides


@Module
class ReviewModule {
    @Provides
    internal fun providesPaymentModule(ReviewPresenter: ReviewPresenter<IReviewView>): IReviewPresenter<IReviewView> =
            ReviewPresenter

}