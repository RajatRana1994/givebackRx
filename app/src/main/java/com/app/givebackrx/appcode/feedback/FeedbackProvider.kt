package com.app.givebackrx.appcode.settings.FeedbackModule

import com.app.givebackrx.appcode.feedback.FeedbackFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FeedbackProvider{

    @ContributesAndroidInjector(modules = [(FeedbackModule::class)])
    abstract fun bindFeedbacksActivity(): FeedbackFragment
}