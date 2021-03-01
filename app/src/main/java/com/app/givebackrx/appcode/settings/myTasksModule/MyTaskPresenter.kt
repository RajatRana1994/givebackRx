package com.app.givebackrx.appcode.settings.myTasksModule


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.TaskEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MyTaskPresenter<V : IMyTaskView>
@Inject internal constructor(private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers), IMyTaskPresenter<V> {


      fun task(data: String="Active") {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().task(data,mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onTaskResponse(it) }, { this.handleError(it,"MyTask") })
        )
    }
    private fun onTaskResponse(it: TaskEntity) {
        mvpView!!.hideLoading()
        when(it.success){
            true->   mvpView!!.onTaskResp(it)
            else ->  mvpView!!.onError("Failed!")
        }
    }


    fun markCompleteTask(task_id: String, position: Int) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().markCompleteTask(task_id,mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onMarkCompleteResponse(it,position) }, { this.handleError(it,"MyTask") })
        )
    }


    private fun onMarkCompleteResponse(
        it: TaskEntity,
        position: Int) {
        mvpView!!.hideLoading()
        when(it.success){
            true->   mvpView!!.onMarkTaskCompleted(it,position)
            else ->  mvpView!!.onError("Failed!")
        }
    }



}