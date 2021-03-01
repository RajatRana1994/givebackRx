package com.app.givebackrx.appcode.settings.favouriteModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavouriteProvider{

    @ContributesAndroidInjector(modules = [(FavouriteModule::class)])
    abstract fun bindFavouritesActivity(): FavouritesFragment
}