package com.app.givebackrx.appcode.signupModule


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import javax.inject.Inject

class SignupPresenter<V : ISignupView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    ISignupPresenter<V> {


    fun signup(jsonObject: HashMap<String,Any>) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().signUp(jsonObject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onSignUpResponse(it) },
                    { this.handleError(it, "preSignUpVerification") })
        )
    }
//

    private fun onSignUpResponse(it: SignInWithUserDetailEntity) {
        mvpView!!.hideLoading()
        if (it.success)
        mvpView!!.onSignupResponse(it)
        else
            mvpView!!.onError(it.message)
    }

    fun postSignUpVerification(phone:String, email:String, referral_code:String) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().postSignUpVerification(phone,email,referral_code)
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
        if (it.success)
        mvpView!!.onPostSignUpVerification(it)
        else
            mvpView!!.onError(it.message)
    }


}