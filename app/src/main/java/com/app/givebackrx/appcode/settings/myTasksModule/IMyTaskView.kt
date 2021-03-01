package com.app.givebackrx.appcode.settings.myTasksModule

import com.app.givebackrx.base.MvpPresenter
import com.app.givebackrx.base.MvpView
import com.app.givebackrx.data.entity.TaskEntity


interface IMyTaskView: MvpView {
    fun onTaskResp(it: TaskEntity)
    fun onMarkTaskCompleted(it: TaskEntity, position: Int)
}

interface IMyTaskPresenter<V: IMyTaskView> : MvpPresenter<V> {
}