package com.app.givebackrx.appcode.settings.referralsModule


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.DashboardEntity
import com.app.givebackrx.data.entity.ReferralEntity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReferralPresenter<V : IReferralView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IReferralPresenter<V> {


    fun referral(data: String = "All") {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .referral(data, mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onReferralResponse(it) }, { this.handleError(it, "Refferal") })
        )
    }

    private fun onReferralResponse(it: ReferralEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onReferralResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }

    fun invitebyReferral(data: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .invitebyReferral(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onInviteReferralResponse(it) }, { this.handleError(it, "Refferal") })
        )
    }

    private fun onInviteReferralResponse(it: SignInWithUserDetailEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onInvitedReferral(it)
            else -> mvpView!!.onError(it.message)
        }
    }


    fun resend(data: String = "All") {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .resend(data, mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onResendReferralResponse(it) }, { this.handleError(it, "Refferal") })
        )
    }

    private fun onResendReferralResponse(it: DashboardEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onResendReferralResp(it)
            else -> mvpView!!.onError(it.message)
        }
    }


}