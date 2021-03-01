package com.app.givebackrx.appcode.mycart


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.CartEntity
import com.app.givebackrx.data.entity.MyCartEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MyCartPresenter<V : IMyCartView>
@Inject internal constructor(private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers), IMyCartPresenter<V> {


    fun getCart(jsonObject:JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().getCart(jsonObject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onGetCartResponse(it) }, { this.handleError(it, "GetCart") })
        )
    }

    private fun onGetCartResponse(it: CartEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onMyCartResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }

    fun updateItemCount(json: JsonObject, action: String, pos: Int) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().addtoCart(json)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onUpdateToCartResponse(it,action,pos) }, { this.handleError(it, "Dashboard") })
        )
    }

    private fun onUpdateToCartResponse(
        it: MyCartEntity,
        action: String,
        pos: Int
    ) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onUpdateToCartResp(it,action,pos)
            else -> mvpView!!.onError("Failed!")
        }
    }
}