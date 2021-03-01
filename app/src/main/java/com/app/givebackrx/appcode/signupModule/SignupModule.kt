package com.app.givebackrx.appcode.signupModule

import dagger.Module
import dagger.Provides

@Module
class SignupModule {
    @Provides
    internal fun providesSignupModule(loginDetailPresenter: SignupPresenter<ISignupView>): ISignupPresenter<ISignupView> = loginDetailPresenter
}