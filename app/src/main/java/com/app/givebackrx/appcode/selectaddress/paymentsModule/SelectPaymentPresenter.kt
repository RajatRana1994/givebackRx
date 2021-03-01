package com.app.givebackrx.appcode.selectaddress.paymentsModule


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.AddressesListEntity
import com.app.givebackrx.data.entity.AllAddressListEntity
import com.app.givebackrx.data.entity.PaymentResEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SelectPaymentPresenter<V : ISelectPaymentsView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    ISelectPaymentsPresenter<V> {


    fun getAllAddress(/*type:String*/) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .getCards(/*mPrefs.getKeyValue(PreferenceConstants.USER_TYPE),type*/)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onAllAddressResponse(it) },
                    { this.handleError(it, "getAllAddress") })
        )
    }

    private fun onAllAddressResponse(it: PaymentResEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onAllPaymentRes(it)
            else -> mvpView!!.onError("Failed!")
        }
    }

    fun deletePayment(id: String, pos: Int) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .deletePaymentCard(id/*, mPrefs.getKeyValue(PreferenceConstants.USER_TYPE)*/)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onDeleteAddressResponse(it, pos) },
                    { this.handleError(it, "deleteAddress") })
        )
    }

    private fun onDeleteAddressResponse(it: SuccessMessageEntity, pos: Int) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onDeleteAddressResp(it,pos)
            else -> mvpView!!.onError("Failed!")
        }
    }

    fun defaultPayment(catalog_order_id: String,card_id: String,screentype:String, pos: Int) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .selectCard(catalog_order_id,card_id /*mPrefs.getKeyValue(PreferenceConstants.USER_TYPE)*/)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onDefaultAddressResponse(it, pos) },
                    { this.handleError(it, "defaultAddress") })
        )
    }


    private fun onDefaultAddressResponse(it: SuccessMessageEntity, pos: Int) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onDefaultAddressResp(it,pos)
            else -> mvpView!!.onError("Failed!")
        }
    }
}