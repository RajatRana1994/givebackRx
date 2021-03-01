package com.app.givebackrx.appcode.printedcard

import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.MyCartEntity
import com.app.givebackrx.data.entity.PrintedCharityEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PrintedCharityPresenter<V : IPrintedCharityView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IPrintedCharityPresenter<V> {


    fun getCardPrintedCharity() {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .getCharityCardList(mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onPrintedCharityResponse(it) }, { this.handleError(it, "PrintedCharity") })
        )
    }

    private fun onPrintedCharityResponse(it: PrintedCharityEntity) {
        mvpView!!.hideLoading()
        mvpView!!.onPrintedCharityResp(it)
    }


    fun addtoCart(json: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().addtoCart(json)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onAddToCartResponse(it) }, { this.handleError(it, "Dashboard") })
        )
    }

    private fun onAddToCartResponse(it: MyCartEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onAddToCartResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }


}