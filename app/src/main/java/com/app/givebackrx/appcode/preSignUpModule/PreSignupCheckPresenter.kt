package com.app.givebackrx.appcode.preSignUpModule


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PreSignupCheckPresenter<V : IPreSignupCheckView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IPreSignupCheckPresenter<V> {


    fun preSignUpVerification(jsonObject: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().preSignUpVerification(jsonObject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onPreSignUpVerification(it) },
                    { this.handleError(it, "preSignUpVerification") })
        )
    }

    //
    private fun onPreSignUpVerification(it: SuccessMessageEntity) {
        mvpView!!.hideLoading()
        mvpView!!.onPreSignUpVerification(it.success)
    }


}