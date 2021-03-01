package com.app.givebackrx.appcode.orderSummary.reviewModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ReviewProvider{

    @ContributesAndroidInjector(modules = [(ReviewModule::class)])
    abstract fun bindReviewActivity(): ReviewFragment
}