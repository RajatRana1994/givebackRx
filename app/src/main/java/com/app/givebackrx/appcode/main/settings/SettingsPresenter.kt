package com.app.givebackrx.appcode.main.settings


import com.google.gson.JsonObject
import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SettingsPresenter<V : ISettingsView>
@Inject internal constructor(private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers), ISettingsPresenter<V> {


//      fun getSettings(jsonObject: JsonObject) {
//        mvpView!!.showLoading()
//        cmp.add(
//            apiHelpers.restService().all_Settingss(jsonObject)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe({ this.onSettingsResponse(it) }, { this.handleError(it,"Settings") })
//        )
//    }
//
//
//    private fun onSettingsResponse(it: AllSettingssEntity) {
//        mvpView!!.hideLoading()
//        when(it.success){
//            true->   mvpView!!.onSettingssResponse(it)
//            else ->  mvpView!!.onError("Settings Failed!")
//        }
//    }



}