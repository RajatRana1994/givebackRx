package com.app.givebackrx.appcode.selectaddress

import dagger.Module
import dagger.Provides


@Module
class SelectAddressModule {
    @Provides
    internal fun providesSelectAddressModule(SelectAddressDetailPresenter: SelectAddressPresenter<ISelectAddressView>): ISelectAddressPresenter<ISelectAddressView> =
        SelectAddressDetailPresenter

}