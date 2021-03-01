package com.app.givebackrx.appcode.settings.changePasswordModule


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.NotificationEntity
import com.app.givebackrx.data.entity.SecurityDetailEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChangePasswordPresenter<V : IChangePasswordView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IChangePasswordPresenter<V> {


    fun changePassword(newpass:String) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().profilePassword(newpass,mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onChangePasswordResponse(it) }, { this.handleError(it, "ChangePassword") })
        )
    }
    fun profileEnableAuth(twoFA:String) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().profileEnableAuth(twoFA,mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onChangePasswordResponse(it) }, { this.handleError(it, "ChangePassword") })
        )
    }
    fun profileSecurity() {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().profileSecurity(mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.OnSecurityDetailResp(it) }, { this.handleError(it, "ChangePassword") })
        )
    }

    fun notificationPreferenceGet() {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().notificationPreferenceGet(mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.OnNotificationGetResp(it) }, { this.handleError(it, "ChangePassword") })
        )
    }

    private fun OnNotificationGetResp(
        it: NotificationEntity
    ) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.OnNotificationGetResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }

    fun notificationPreferenceSet(
        promotions: String,
        coupon_experience: String,
        givebackrx_news: String,
        type: Int
    ) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().notificationPreferenceSet(promotions,coupon_experience,givebackrx_news,mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.OnNotificationResp(it, type) }, { this.handleError(it, "ChangePassword") })
        )
    }

    private fun OnNotificationResp(
        it: SuccessMessageEntity,
        type: Int
    ) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.OnNotificationResp(it,type)
            else -> mvpView!!.onError("Failed!")
        }
    }


    private fun onChangePasswordResponse(it: SuccessMessageEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onUpdatedSuccess(it)
            else -> mvpView!!.onError("Failed!")
        }
    }
    private fun OnSecurityDetailResp(it: SecurityDetailEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onSecuritySuccess(it)
            else -> mvpView!!.onError("Failed!")
        }
    }



}