package com.app.givebackrx.appcode.main.charity


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.CharityListEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CharityPresenter<V : ICharityView>
@Inject internal constructor(private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers), ICharityPresenter<V> {

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

    private fun onGotCharityListResponse(it: CharityListEntity) {
        mvpView!!.hideLoading()
        if (it.success)
            mvpView!!.onGotCharityList(it)
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






}