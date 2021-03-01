package com.app.givebackrx.appcode.settings.updateCardModule

import com.app.givebackrx.appcode.main.settings.updateCardModule.UpdateCardFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UpdateCardProvider{

    @ContributesAndroidInjector(modules = [(UpdateCardModule::class)])
    abstract fun bindUpdateCardsActivity(): UpdateCardFragment
}