package com.app.givebackrx.appcode.settings.myTasksModule

import dagger.Module
import dagger.Provides


@Module
class MyTaskModule {
    @Provides
    internal fun providesMyTaskModule(MyTaskDetailPresenter: MyTaskPresenter<IMyTaskView>): IMyTaskPresenter<IMyTaskView> =
        MyTaskDetailPresenter

}