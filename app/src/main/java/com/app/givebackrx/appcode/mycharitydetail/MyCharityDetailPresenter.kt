package com.app.givebackrx.appcode.mycharitydetail


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.MyCharityDetailEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MyCharityDetailPresenter<V : IMyCharityDetailView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IMyCharityDetailPresenter<V> {

    fun registeredCharityDetail() {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().registeredCharityDetail()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onCharityDetailResponse(it) },
                    { this.handleError(it, "preSignUpVerification") })
        )
    }

    //
    private fun onCharityDetailResponse(it: MyCharityDetailEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onMyCharityDetailResponse(it)
            else -> mvpView!!.onError(it.message)
        }
    }


    fun emailCard(data: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().emailCard(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onCardViaEmailResponse(it) },
                    { this.handleError(it, "CardViaEmail") })
        )
    }

    fun textCard(data: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().textCard(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onCardViaEmailResponse(it) },
                    { this.handleError(it, "CardViaEmail") })
        )
    }

    private fun onCardViaEmailResponse(it: SuccessMessageEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onCardViaEmailed(it)
            else -> mvpView!!.onError("Failed!")
        }
    }

}