package com.app.givebackrx.appcode.settings.dashboard


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.DashboardEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DashboardPresenter<V : IDashboardView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IDashboardPresenter<V> {


    fun dashboard() {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().dashboard(mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onDashboardResponse(it) }, { this.handleError(it, "Dashboard") })
        )
    }

    private fun onDashboardResponse(it: DashboardEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onDashboardResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }


    fun emailCard(data: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().emailCard(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onCardViaEmailResponse(it) }, { this.handleError(it,"CardViaEmail") })
        )
    }

    fun textCard(data: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().textCard(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onCardViaEmailResponse(it) }, { this.handleError(it,"CardViaEmail") })
        )
    }

    private fun onCardViaEmailResponse(it: SuccessMessageEntity) {
        mvpView!!.hideLoading()
        when(it.success){
            true->   mvpView!!.onCardViaEmailed(it)
            else ->  mvpView!!.onError("Failed!")
        }
    }
}