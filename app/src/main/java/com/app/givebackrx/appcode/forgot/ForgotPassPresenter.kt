package com.app.givebackrx.appcode.forgot


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ForgotPassPresenter<V : IForgotPassView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IForgotPassPresenter<V> {


    fun forgotPassword(email: String) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().forgotPassword(value = email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.oForgotPassResponse(it) },
                    { this.handleError(it, "forgotpassword") })
        )
    }

    //
    private fun oForgotPassResponse(it: SignInWithUserDetailEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onForgotPassResp(it)
            else -> mvpView!!.onError(it.message)
        }
    }



}