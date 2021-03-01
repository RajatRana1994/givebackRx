package com.app.givebackrx.appcode.drugDetail


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.CouponGenerationEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DrugDetailPresenter<V : IDrugDetailView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IDrugDetailPresenter<V> {


    fun couponViaEmail(data: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().couponViaEmail(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onCouponViaEmailResponse(it) },
                    { this.handleError(it, "DrugDetail") })
        )
    }
    fun couponViaPhone(data: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().couponViaPhone(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onCouponViaEmailResponse(it) },
                    { this.handleError(it, "couponViaPhone") })
        )
    }

    fun addFaveMed(data: JsonObject) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().addFavoriteMedication(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onAddFavMedResponse(it) }, { this.handleError(it, "DrugDetail") })
        )
    }

    private fun onAddFavMedResponse(it: SuccessMessageEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onAddedFavResp(it)
            else -> mvpView!!.onError("Failed!")
        }
    }

    private fun onCouponViaEmailResponse(it: SuccessMessageEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onCouponViaEmailed(it)
            else -> mvpView!!.onError("Failed!")
        }
    }


    fun couponGenerate(drug_name: HashMap<String, String>) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().couponGeneration(drug_name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { this.onCouponGenerateResponse(it) },
                    { this.handleError(it, "DrugDetail") })
        )
    }


    private fun onCouponGenerateResponse(it: CouponGenerationEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onCouponGenerated(it)
            else -> mvpView!!.onError("Failed!")
        }
    }


}