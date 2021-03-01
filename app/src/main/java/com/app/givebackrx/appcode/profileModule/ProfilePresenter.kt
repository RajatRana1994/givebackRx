package com.app.givebackrx.appcode.profileModule


import com.app.givebackrx.base.BasePresenter
import com.app.givebackrx.data.entity.ProfileEntity
import com.app.givebackrx.data.entity.ProfileUpdatedEntity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.entity.VerifySecondaryOtpEntity
import com.app.givebackrx.data.network.ApiHelper
import com.app.givebackrx.data.preferences.AppPreferenceHelper
import com.app.givebackrx.utils.PreferenceConstants
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ProfilePresenter<V : IProfileView>
@Inject internal constructor(
    private val mPrefs: AppPreferenceHelper, val cmp: CompositeDisposable, val apiHelpers: ApiHelper
) : BasePresenter<V>(mPrefs, compositeDisposable = cmp, apiHelper = apiHelpers),
    IProfilePresenter<V> {


    fun profileDetail() {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .profileDetail(mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onProfileResponse(it) }, { this.handleError(it, "Profile") })
        )
    }

    fun editProfile(first_name:String,
                    last_name:String,
                    address:String,
                    city:String,
                    state:String,
                    country:String,
                    dob:String,
                    gender:String,
                    apt_suite_number:String,
                    postal_code:String) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .editProfile(first_name, last_name, address, city, state, country, dob,gender, apt_suite_number, postal_code,mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onEditProfileResponse(it) }, { this.handleError(it, "Profile") })
        )
    }

    private fun onEditProfileResponse(it: VerifySecondaryOtpEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onEditProfileResponse(it)
            else -> mvpView!!.onError("Profile Failed!")
        }
    }
    private fun onProfileResponse(it: ProfileEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onProfileResponse(it)
            else -> mvpView!!.onError("Profile Failed!")
        }
    }

    fun editProfileImage(profileImage: String) {
        val imageToUpload =
            RequestBody.create(MediaType.parse("image/jpeg"), File(profileImage).absoluteFile)
        val usertype = RequestBody.create(
            MediaType.parse("text/plain"),
            mPrefs.getKeyValue(PreferenceConstants.USER_TYPE)
        )

        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService().editProfileImage(usertype, imageToUpload)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onProfileImageResponse(it) }, { this.handleError(it, "Profile") })
        )
    }

    fun updateSecondary(type: String, value: String) {
        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .updateSecondary(type, value, mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onUpdateSecondaryResponse(it,value,type) }, { this.handleError(it, "Profile") })
        )
    }

    private fun onUpdateSecondaryResponse(
        it: VerifySecondaryOtpEntity,
        value: String,
        type: String
    ) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onUpdateSecondaryResp(it,value,type)
            else -> mvpView!!.onError("Profile Failed!")
        }
    }
    private fun onProfileImageResponse(it: ProfileUpdatedEntity) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onProfileImageResponse(it)
            else -> mvpView!!.onError("Profile Failed!")
        }
    }

    fun verifySecondaryOTP(
        data: JsonObject,
        email: String,
        type: String
    ) {

        mvpView!!.showLoading()
        cmp.add(
            apiHelpers.restService()
                .verifySecondaryOTP(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.onVerifySecondaryOTP(it,email,type) }, { this.handleError(it, "Profile") })
        )
    }


    private fun onVerifySecondaryOTP(
        it: SignInWithUserDetailEntity,
        email: String,
        type: String
    ) {
        mvpView!!.hideLoading()
        when (it.success) {
            true -> mvpView!!.onVerifySecondaryOTPResp(it,email,type)
            else -> mvpView!!.onError("Profile Failed!")
        }
    }


}