package com.app.givebackrx.appcode.orderSummary.shippingModule

import com.app.givebackrx.appcode.payment.IPaymentPresenter
import dagger.Module
import dagger.Provides


@Module
class ShipmentModule {
    @Provides
    internal fun providesPaymentModule(ShipmentPresenter: ShipmentPresenter<IShippmentView>): IShipmentPresenter<IShippmentView> =
        ShipmentPresenter

}