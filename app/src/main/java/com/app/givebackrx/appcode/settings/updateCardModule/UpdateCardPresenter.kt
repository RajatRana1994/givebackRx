package com.app.givebackrx.appcode.settings.updateCardModule


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.*
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UpdateCardPresenter<V : IUpdateCardView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IUpdateCardPresenter<V> {


    fun dashboard() {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().dashboard(mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onDashboardResponse(it) }, { this.handleError(it, "Dashboard") })
        )
    }

    fun goldCardPaymentSummary(coupon:String) {
        mvpView!!.showLoading()
        cmp.add(
                apiHelpers.restService()
                        .getGoldMembershipSummary(coupon, "membership")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ this.goldCardPaymentSummaryResponse(it) }, { this.handleError(it, "Dashboard") })
        )
    }

    private fun goldCardPaymentSummaryResponse(it: MemberShipOrderEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onMembershipResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }

    private fun onDashboardResponse(it: DashboardEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onDashboardResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }


    fun emailCard(data: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().emailCard(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onCardViaEmailResponse(it) },
                    { this.handleError(it, "CardViaEmail") })
        )
    }

    fun textCard(data: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().textCard(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onCardViaEmailResponse(it) },
                    { this.handleError(it, "CardViaEmail") })
        )
    }

    private fun onCardViaEmailResponse(it: SuccessMessageEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onCardViaEmailed(it)
            else -> mvpView!!.onError("Failed!")
        }
    }


    fun getCart(jsonObject: JsonObject) {
//        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().getCart(jsonObject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onGetCartResponse(it) }, { this.handleError(it, "GetCart") })
        )
    }

    private fun onGetCartResponse(it: CartEntity) {
//        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onSavedCardsResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }

    fun deleteSavedCard(cardid: String, position: Int) {
//        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .deleteSavedCard(cardid, mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onDeleteSavedCardResponse(it, position) },
                    { this.handleError(it, "GetCart") })
        )
    }

    private fun onDeleteSavedCardResponse(
        it: FavoriteMedicationEntity,
        position: Int
    ) {
//        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onDeletedCardResp(it, position)
            else -> mvpView!!.onError("Failed!")
        }
    }

    fun disableRecurringPayment(feedback: String) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().disableRecurringPayment(
                feedback,
                mPrefs.getKeyValue(PreferenceConstants.USER_TYPE)
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onDisableRecurringPaymentResponse(it) },
                    { this.handleError(it, "GetCart") })
        )
    }

    private fun onDisableRecurringPaymentResponse(it: SuccessMessageEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onDisableRecurringPaymentResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }

    fun updateCards(
        first_name: String,
        last_name: String,
        card_number: String,
        expire_month: String,
        expire_year: String,
        cvv: String,
        card_type: String
    ) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .updateCards(first_name,last_name,card_number,expire_month,expire_year,cvv,card_type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onUpdateCardsResponse(it) }, { this.handleError(it, "GetCart") })
        )
    }

    private fun onUpdateCardsResponse(it: SuccessMessageEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onUpdateCardResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }


}