package com.app.givebackrx.appcode.storecheckout


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.FavoriteMedicationEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StoreCheckoutPresenter<V : IStoreCheckoutView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IStoreCheckoutPresenter<V> {


//    fun StoreCheckout() {
//        mvpView!!.showLoading()
//        cmp.add(
//            apiHelpers.restService()
//                .StoreCheckout(mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(
//                    { this.onStoreCheckoutResponse(it) },
//                    { this.handleError(it, "StoreCheckout") })
//        )
//    }
//
//    private fun onStoreCheckoutResponse(it: StoreCheckoutEntity) {
//        mvpView!!.hideLoading()
//        when (it.success) {
//            true -> mvpView!!.onStoreCheckoutResp(it)
//            else -> mvpView!!.onError("Failed!")
//        }
//    }


    fun paymentCart(data: HashMap<String,Any>) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().paymentCart(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onCartPaymentResponse(it) },
                    { this.handleError(it, "addNewAddress") })
        )
    }

    private fun onCartPaymentResponse(it: SuccessMessageEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onCartPaymentResp(it)
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