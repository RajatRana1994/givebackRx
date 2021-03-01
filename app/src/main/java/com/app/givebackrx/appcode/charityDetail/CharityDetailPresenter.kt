package com.app.givebackrx.appcode.charityDetail


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.CharityDetailEntity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CharityDetailPresenter<V : ICharityDetailView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    ICharityDetailPresenter<V> {

    fun charityDetail(charity_id: String) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().charityDetail(charity_id,if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE) else "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onCharityDetailResponse(it) },
                    { this.handleError(it, "preSignUpVerification") })
        )
    }
    fun charityDetailWithoutToken(charity_id: String) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().charityDetail(charity_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onCharityDetailResponse(it) },
                    { this.handleError(it, "preSignUpVerification") })
        )
    }

    //
    private fun onCharityDetailResponse(it: CharityDetailEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onCharityDetailResponse(it.data,it.auth)
            else -> mvpView!!.onError(it.message)
        }
    }

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


    fun selectCharity(charity_id:String) {
        mvpView!!.showLoading()
        cmp.add(apiHelpers.restService().selectCharity(charity_id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { this.onSelectCharityResponse(it) },
                { this.handleError(it, "selectCharity") })
        )
    }
//

    private fun onSelectCharityResponse(it: SuccessMessageEntity) {
        mvpView!!.hideLoading()
        if (it.success)
            mvpView!!.onSelectedCharity(it)
        else
            mvpView!!.onError(it.message)
    }

    fun emailCard(data: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().emailCard(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onCardViaEmailResponse(it) }, { this.handleError(it,"CardViaEmail") })
        )
    }
    fun textCard(data: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().textCard(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onCardViaEmailResponse(it) }, { this.handleError(it,"CardViaEmail") })
        )
    }
    private fun onCardViaEmailResponse(it: SuccessMessageEntity) {
        mvpView!!.hideLoading()
        when(it.success){
            true->   mvpView!!.onCardViaEmailed(it)
            else ->  mvpView!!.onError("Failed!")
        }
    }


}