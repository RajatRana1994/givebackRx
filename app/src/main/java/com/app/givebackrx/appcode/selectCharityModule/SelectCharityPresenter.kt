package com.app.givebackrx.appcode.selectCharityModule


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.CharityListEntity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import javax.inject.Inject

class SelectCharityPresenter<V : ISelectCharityView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    ISelectCharityPresenter<V> {


    fun getCharityList(sort_by_alphabet:String, page_number:String, membership_type:String,
                       sort_by_category:String, state:String, city:String, charity_name:String) {
        mvpView!!.showLoading()
        cmp.add(apiHelpers.restService().charityList(sort_by_alphabet, page_number, membership_type, sort_by_category, state, city, charity_name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onGotCharityListResponse(it) },
                    { this.handleError(it, "getCharityList") })
        )
    }
//

    private fun onGotCharityListResponse(it: CharityListEntity) {
        mvpView!!.hideLoading()
        if (it.success)
            mvpView!!.onGotCharityList(it)
        else
            mvpView!!.onError(it.message)
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

}