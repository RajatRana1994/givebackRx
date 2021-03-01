package com.app.givebackrx.appcode.settings.FeedbackModule


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FeedbackPresenter<V : IFeedbackView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IReferralPresenter<V> {


    fun feedback(rating: String, desc: String) {
        val json=JsonObject().apply {
            addProperty("rating",rating)
            addProperty("description",desc)
            if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
            addProperty("membership_type",mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE))
        }
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .feedback(json)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onFavoriteMedication(it) }, { this.handleError(it, "feedback") })
        )
    }

    private fun onFavoriteMedication(it: SignInWithUserDetailEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onFeedbackResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }

}