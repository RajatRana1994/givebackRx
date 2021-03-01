package com.app.givebackrx.appcode.charitysignee


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody
import org.json.JSONObject
import javax.inject.Inject

class CharitySigneePresenter<V : ICharitySigneeView>
@Inject internal constructor(private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers), ICharitySigneePresenter<V> {


      fun charitySignee(data: HashMap<String, RequestBody>) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().charitySignee(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onCouponViaEmailResponse(it) }, { this.handleError(it,"CharitySignee") })
        )
    }
    private fun onCouponViaEmailResponse(it: SuccessMessageEntity) {
        mvpView!!.hideLoading()
        when(it.success){
//            true->   mvpView!!.onCouponViaEmailed(it)
            else ->  mvpView!!.onError("Failed!")
        }
    }




}