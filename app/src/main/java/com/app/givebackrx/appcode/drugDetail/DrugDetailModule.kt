package com.app.givebackrx.appcode.drugDetail

import dagger.Module
import dagger.Provides


@Module
class DrugDetailModule {
    @Provides
    internal fun providesDrugDetailModule(DrugDetailDetailPresenter: DrugDetailPresenter<IDrugDetailView>): IDrugDetailPresenter<IDrugDetailView> =
        DrugDetailDetailPresenter

}