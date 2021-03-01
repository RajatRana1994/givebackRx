package com.app.givebackrx.appcode.orderSummary.reviewModule

import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.*
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReviewPresenter<V : IReviewView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IReviewPresenter<V> {


    fun getOrderSummary(type: String) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .getOrderSummary(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onStoreSummary(it) }, { this.handleError(it, "Payment") })
        )
    }

    fun getOrderSummaryService(type: String,id:String) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .getOrderSummaryTwo(type,id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onStoreSummary(it) }, { this.handleError(it, "Payment") })
        )
    }

    private fun onStoreSummary(it: StoreOrderSummary) {
        mvpView!!.hideLoading()
        mvpView!!.onOrderStoreSummary(it)
    }

    fun storePayment(data: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
                apiHelpers.restService()
                        .storePayment(data)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ this.onPayResponse(it) }, { this.handleError(it, "Refferal") })
        )
    }

    private fun onPayResponse(it: SuccessMessageEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onOrderStorePayment(it)
            else -> mvpView!!.onError(it.message)
        }
    }


    fun storePaymentHold(catalog_order_id:String) {
        mvpView!!.showLoading()
        cmp.add(
                apiHelpers.restService()
                        .storePaymentResponse(catalog_order_id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ this.onPayHoldResponse(it) }, { this.handleError(it, "Refferal") })
        )
    }

    private fun onPayHoldResponse(it: HoldResponseEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onOrderStorePaymentHold(it)
            else -> mvpView!!.onError(it.message)
        }
    }

}