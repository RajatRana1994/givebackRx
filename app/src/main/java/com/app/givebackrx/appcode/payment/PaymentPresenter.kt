package com.app.givebackrx.appcode.payment

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

class PaymentPresenter<V : IPaymentView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IPaymentPresenter<V> {


    fun goldCardPayment(backend: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .goldCardPayment(backend)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onPaymentResponse(it) }, { this.handleError(it, "Payment") })
        )
    }

    fun goldCardPaymentSummary(coupon:String) {
        mvpView!!.showLoading()
        cmp.add(
                apiHelpers.restService()
                        .getGoldSummary(coupon, "membership")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ this.goldCardPaymentSummary(it) }, { this.handleError(it, "Payment") })
        )
    }


    private fun goldCardPaymentSummary(it: OrderSummaryModel) {
                mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.goldCardPaymentSummary(it)
            else -> mvpView!!.goldCardPaymentSummaryError(it)
        }
    }



    private fun onPaymentResponse(it: PaymentEntity) {
        mvpView!!.hideLoading()
        mvpView!!.onPaymentResp(it)
    }


    fun goldCardPaymentSummaryDetail(coupon:String) {
        mvpView!!.showLoading()
        cmp.add(
                apiHelpers.restService()
                        .getGoldMembershipSummary(coupon, "membership")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ this.goldCardPaymentSummaryResponse(it) }, { this.handleError(it, "Dashboard") })
        )
    }

    private fun goldCardPaymentSummaryResponse(it: MemberShipOrderEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onMembershipResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }


    fun getCart(jsonObject: JsonObject) {
//        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().getCart(jsonObject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onGetCartResponse(it) }, { this.handleError(it, "GetCart") })
        )
    }

    private fun onGetCartResponse(it: CartEntity) {
//        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onSavedCardsResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }

    fun deleteSavedCard(cardid: String, position: Int) {
//        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .deleteSavedCard(cardid, mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onDeleteSavedCardResponse(it, position) },
                    { this.handleError(it, "GetCart") })
        )
    }

    private fun onDeleteSavedCardResponse(
        it: FavoriteMedicationEntity,
        position: Int
    ) {
//        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onDeletedCardResp(it, position)
            else -> mvpView!!.onError("Failed!")
        }
    }


}