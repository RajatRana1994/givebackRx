package com.app.givebackrx.appcode.settings.myNoticesModule


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.NoticeEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MyNoticesPresenter<V : IMyNoticesView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IMyNoticesPresenter<V> {


    fun notices(data: String = "All") {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .notices(data, mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onNoticeResponse(it) }, { this.handleError(it, "MyNotices") })
        )
    }

    private fun onNoticeResponse(it: NoticeEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onNoticeResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }


    fun acknowledgeNotices(data: String = "All", position: Int) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .acknowledgeNotices(data, mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onAcknowledgeNoticeResponse(it,position) },
                    { this.handleError(it, "MyNotices") })
        )
    }

    private fun onAcknowledgeNoticeResponse(
        it: NoticeEntity,
        position: Int
    ) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onNoticeAcknowledged(it,position)
            else -> mvpView!!.onError("Failed!")
        }
    }


}