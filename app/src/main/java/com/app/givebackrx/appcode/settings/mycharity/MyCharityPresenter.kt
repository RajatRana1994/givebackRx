package com.app.givebackrx.appcode.settings.mycharity


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.MyCharityEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MyCharityPresenter<V : IMyCharityView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IMyCharityPresenter<V> {


    fun myCharity() {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .myCharity()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onMyCharityResponse(it) }, { this.handleError(it, "MyCharity") })
        )
    }

    private fun onMyCharityResponse(it: MyCharityEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onMyCharityResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }


}