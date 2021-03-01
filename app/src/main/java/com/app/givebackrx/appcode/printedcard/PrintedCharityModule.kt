package com.app.givebackrx.appcode.printedcard

import dagger.Module
import dagger.Provides


@Module
class PrintedCharityModule {
    @Provides
    internal fun providesPrintedCharityModule(PrintedCharityDetailPresenter: PrintedCharityPresenter<IPrintedCharityView>): IPrintedCharityPresenter<IPrintedCharityView> =
        PrintedCharityDetailPresenter

}