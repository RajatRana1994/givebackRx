package com.app.givebackrx.appcode.orderSummary.addPaymentModule


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddNewPaymentPresenter<V : IAddNewPaymentView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IAddNewPaymentPresenter<V> {


    fun addNewAddress(data: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().addNewCard(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onAddedAddressResponse(it) },
                    { this.handleError(it, "addNewAddress") })
        )
    }


    private fun onAddedAddressResponse(it: SuccessMessageEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onAddedCardResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }
}