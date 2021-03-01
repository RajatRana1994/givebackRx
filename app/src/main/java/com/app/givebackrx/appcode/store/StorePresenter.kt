package com.app.givebackrx.appcode.store


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.StoreListEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StorePresenter<V : IStoreView>
@Inject internal constructor(private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers), IStorePresenter<V> {


    fun storeListing(map:HashMap<String,String>) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().storeListing(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onDashboardResponse(it) }, { this.handleError(it, "Dashboard") })
        )
    }

    private fun onDashboardResponse(it: StoreListEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onStoreListResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }


}