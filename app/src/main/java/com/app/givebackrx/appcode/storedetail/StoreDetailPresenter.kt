package com.app.givebackrx.appcode.storedetail


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.MyCartEntity
import com.app.givebackrx.data.entity.StoreDetailEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StoreDetailPresenter<V : IStoreDetailView>
@Inject internal constructor(private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers), IStoreDetailPresenter<V> {


    fun storeProductDetail(map:HashMap<String,String>) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().storeProductDetail(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onProductDetailResponse(it) }, { this.handleError(it, "Dashboard") })
        )
    }

    private fun onProductDetailResponse(it: StoreDetailEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onStoreDetailResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }

    fun addtoCart(json:JsonObject) {
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