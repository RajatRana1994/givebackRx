package com.app.givebackrx.appcode.verifyOtpModule


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class VerifyOTPPresenter<V : IVerifyOTPView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IVerifyOTPPresenter<V> {


    fun sendAgain(jsonObject: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().sendAgain(jsonObject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onSendOtpResponse(it) },
                    { this.handleError(it, "sendAgain") })
        )
    }

    private fun onSendOtpResponse(it: SignInWithUserDetailEntity) {
        mvpView!!.hideLoading()
        if (it.success)
            mvpView!!.showMessage(it.message)
        else
            mvpView!!.onError(it.message)
    }


    fun verifyOTP(jsonObject: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().verifyOTP(jsonObject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onVerifyOTPResponse(it) },
                    { this.handleError(it, "preVerifyOTPVerification") })
        )
    }
//


 private fun onVerifyOTPResponse(it: SignInWithUserDetailEntity) {
        mvpView!!.hideLoading()
        if (it.success)
            mvpView!!.onVerifyOTPResponse(it.data)
        else
            mvpView!!.onError(it.message)
    }


}