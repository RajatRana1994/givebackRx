package com.app.givebackrx.appcode.settings.favouriteModule


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.DashboardEntity
import com.app.givebackrx.data.entity.FavoriteMedicationEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FavouritePresenter<V : IFavouriteView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IReferralPresenter<V> {


    fun getFavoriteMedication(showLoading:Boolean) {
        if (showLoading)
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .getFavoriteMedication(mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE), mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onFavoriteMedication(it) }, { this.handleError(it, "Refferal") })
        )
    }

    private fun onFavoriteMedication(it: FavoriteMedicationEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onFavoriteMedicationResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }

    fun deleteFavoriteMedication(fav_id: String, position: Int) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .deleteFavoriteMedication(fav_id,mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onDeleteFavoriteMedicationResponse(it,position) }, { this.handleError(it, "Refferal") })
        )
    }

    private fun onDeleteFavoriteMedicationResponse(
        it: FavoriteMedicationEntity,
        position: Int
    ) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onDeleteFavoriteMedicationResp(it,position)
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
//            true -> mvpView!!.onResendReferralResp(it)
            else -> mvpView!!.onError(it.message)
        }
    }


}