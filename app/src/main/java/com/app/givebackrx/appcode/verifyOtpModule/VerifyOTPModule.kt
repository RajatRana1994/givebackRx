package com.app.givebackrx.appcode.verifyOtpModule

import dagger.Module
import dagger.Provides

@Module
class VerifyOTPModule {
    @Provides
    internal fun providesVerifyOTPModule(loginDetailPresenter: VerifyOTPPresenter<IVerifyOTPView>): IVerifyOTPPresenter<IVerifyOTPView> = loginDetailPresenter
}