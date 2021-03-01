package com.app.givebackrx.appcode.main.pharmacy


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.FavoriteMedicationEntity
import com.app.givebackrx.data.entity.PharmacyListEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PharmacyPresenter<V : IPharmacyView>
@Inject internal constructor(private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers), IPharmacyPresenter<V> {


      fun pharmacyList(map:HashMap<String,String>/*drug_generic_name: String,drug_name: String,is_location: String,form: String
          ,dosage: String,quantity: String,latitude: String,longitude: String*/) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().pharmacyList(map)
//                drug_generic_name
//                    ,drug_name
//                    ,is_location
//                    ,form
//                    ,dosage
//                    ,quantity
//                    ,latitude
//                    ,longitude
//                    ,mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onDrugDetailResponse(it) }, { this.handleError(it,"DrugDetail") })
        )
    }



    private fun onDrugDetailResponse(it: PharmacyListEntity) {
        mvpView!!.hideLoading()
        when(it.success){
            true->   mvpView!!.onPharmacyResponse(it.data,it.auth)
            else ->  mvpView!!.onError("DrugDetail Failed!")
        }
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

    fun deleteFavoriteMedication(favoriteMedicationId: String) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .deleteFavoriteMedication(favoriteMedicationId,mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onDeleteFavoriteMedicationResponse(it) }, { this.handleError(it, "Refferal") })
        )
    }

    private fun onDeleteFavoriteMedicationResponse(
        it: FavoriteMedicationEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onDeleteFavoriteMedicationResp(it)
            else -> mvpView!!.onError(it.message)
        }
    }
}