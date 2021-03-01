package com.app.givebackrx.appcode.settings.help


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.HelpEntity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HelpPresenter<V : IHelpView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IHelpPresenter<V> {


    fun getFaq(
        customer: String,
        pharmacy: String,
        charity: String,
        partner_in_care: String
    ) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().getFaq(
                customer,
                pharmacy,
                charity,
                partner_in_care, mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onHelpResponse(it) }, { this.handleError(it, "Help") })
        )
    }

    private fun onHelpResponse(it: HelpEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onHelpResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }

    fun helpTicket(jsonObject: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(apiHelpers.restService().helpTicket(jsonObject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onHelpTicketResponse(it) }, { this.handleError(it, "Help") })
        )
    }

    private fun onHelpTicketResponse(it: SignInWithUserDetailEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onHelpTicketResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }


}