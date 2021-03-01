package com.app.givebackrx.appcode.selectaddress.addnewaddress

import dagger.Module
import dagger.Provides


@Module
class AddNewAddressModule {
    @Provides
    internal fun providesSelectAddressModule(SelectAddressDetailPresenter: AddNewAddressPresenter<IAddNewAddressView>): IAddNewAddressPresenter<IAddNewAddressView> =
        SelectAddressDetailPresenter

}