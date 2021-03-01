package com.app.givebackrx.appcode.selectaddress


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.AddressesListEntity
import com.app.givebackrx.data.entity.AllAddressListEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SelectAddressPresenter<V : ISelectAddressView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    ISelectAddressPresenter<V> {


    fun getAllAddress(type:String) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .getAllAddress(mPrefs.getKeyValue(PreferenceConstants.USER_TYPE),type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onAllAddressResponse(it) },
                    { this.handleError(it, "getAllAddress") })
        )
    }

    fun getAllAddressTwo(type:String,id:String) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .getAllAddressTwo(mPrefs.getKeyValue(PreferenceConstants.USER_TYPE),type,id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onAllAddressResponse(it) },
                    { this.handleError(it, "getAllAddress") })
        )
    }

    private fun onAllAddressResponse(it: AddressesListEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onAllAddressResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }

     private fun onAllAddressResponseTwo(it: AddressesListEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onAllAddressResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }

    fun deleteAddress(address_id: String, pos: Int) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .deleteAddress(address_id, mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
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

    fun defaultAddress(address_id: String,screentype:String, pos: Int) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .defaultAddress(address_id,screentype, mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
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