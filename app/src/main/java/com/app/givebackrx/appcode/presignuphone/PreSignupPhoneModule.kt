package com.app.givebackrx.appcode.presignuphone

import com.app.givebackrx.appcode.preSignUpModule.IPreSignupCheckPresenter
import com.app.givebackrx.appcode.preSignUpModule.IPreSignupCheckView
import com.app.givebackrx.appcode.preSignUpModule.PreSignupCheckPresenter
import dagger.Module
import dagger.Provides


@Module
class PreSignupPhoneModule {
    @Provides
    internal fun providesPreSignupCheckModule(loginDetailPresenter: PreSignupCheckPresenter<IPreSignupCheckView>): IPreSignupCheckPresenter<IPreSignupCheckView> =
        loginDetailPresenter

}