package com.app.givebackrx.appcode.main.pharmacy

import dagger.Module
import dagger.Provides


@Module
class PharmacyModule {
    @Provides
    internal fun providesDrugDetailModule(DrugDetailDetailPresenter: PharmacyPresenter<IPharmacyView>): IPharmacyPresenter<IPharmacyView> =
        DrugDetailDetailPresenter

}