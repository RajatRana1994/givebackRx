package com.app.givebackrx.appcode.settings.nearme


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.DashboardEntity
import com.app.givebackrx.data.entity.NearMeEntity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NearMePresenter<V : INearMeView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    INearMePresenter<V> {


    fun pharmacyNearMe(lat:String,lng:String,distance:String="5",zip:String) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .pharmacyNearMe(lat,lng,distance,zip)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onNearMeResponse(it) }, { this.handleError(it, "Refferal") })
        )
    }

    private fun onNearMeResponse(it: NearMeEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onNearMeResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }



}