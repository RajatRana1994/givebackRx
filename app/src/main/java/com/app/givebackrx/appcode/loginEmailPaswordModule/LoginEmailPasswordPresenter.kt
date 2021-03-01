package com.app.givebackrx.appcode.loginEmailPaswordModule


import android.util.Log
import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.SignInWithUserDetailData
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class LoginEmailPasswordPresenter<V : ILoginEmailPasswordView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    ILoginEmailPasswordPresenter<V> {


    fun signIn(jsonObject: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().signIn(jsonObject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onSignInResponse(it) },
                    { this.handleSigninError(it, "preSignUpVerification") })
        )
    }

    //
    private fun onSignInResponse(it: SignInWithUserDetailEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onSignInWithUserDetail(it.data)
            else -> mvpView!!.onError(it.message)
        }
    }

    fun handleSigninError(e: Throwable, lastAction: String) {
        e.printStackTrace()
        try {
            mvpView!!.hideLoading()
            if (lastAction.equals("logout")) {
                mPrefs.logoutUser()
                mvpView!!.onLogout("")
                return
            }
            if (e is HttpException) {
                val responseBody = e.response()!!.errorBody()
                if (e.code() == 401 || e.localizedMessage!!.contains("401 Unauthorized", false)) {
                    android.os.Handler().postDelayed({
                        authDetail(lastAction)
                    }, 1200)
                } else if (e.code() == 400) {
                    val errorStr = responseBody!!.string()
                    val message = (JSONObject(errorStr).getString("message"))
                    val user_type =if((JSONObject(errorStr).get("data") as JSONObject).has("user_type"))
                        (JSONObject(errorStr).get("data") as JSONObject).getString("user_type") else "portalUser"
                    if ((JSONObject(errorStr).get("data") as JSONObject).has("two_factor_auth")||(JSONObject(errorStr).get("data") as JSONObject).has("otp")) {
                        val twoFactor =
                            if ((JSONObject(errorStr).get("data") as JSONObject).has("two_factor_auth"))
                                (JSONObject(errorStr).get("data") as JSONObject).getBoolean("two_factor_auth") else false
                        mvpView!!.onSignInWithUserDetail(
                            SignInWithUserDetailData(
                                user_type = user_type,
                                two_factor_auth = twoFactor
                            )
                        )
                    }else
                    mvpView!!.showErrorMessage(message)
                } else {
                    mvpView!!.showErrorMessage(getErrorMessage(responseBody))
                }
            } else {
                e.localizedMessage.let {
                    mvpView!!.onError(e.localizedMessage!!)
                }
            }
            Log.e("HandleError", e.message!!)
        } catch (e1: NullPointerException) {
            e1.printStackTrace()
        }
        cmp.clear()
    }


}