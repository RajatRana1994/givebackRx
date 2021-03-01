package com.app.givebackrx.base

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.givebackrx.R
import com.app.givebackrx.appcode.loginEmailModule.LoginEmailActivity
import com.app.givebackrx.appcode.loginEmailPaswordModule.LoginEmailPasswordActivity
import com.app.givebackrx.appcode.preSignUpModule.PreSignUpCheckActivity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.preferences.PreferenceHelper
import com.app.givebackrx.utils.*
import com.app.givebackrx.utils.extension.toast
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), MvpView, BaseFragment.BaseCallback {

    private var mProgressDialog: ProgressDialog? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    @Inject
    lateinit var mPrefs: PreferenceHelper

    @Inject
    lateinit var permissionFile: PermissionFile

    @Inject
    lateinit var mAppUtils: AppUtils

    @Inject
    lateinit var mDialogsUtil: DialogsUtil

    @Inject
    lateinit var mImageUtility: ImageUtility

    //    @Inject
//    lateinit var permissionFile: PermissionFile
    var userDta: SignInWithUserDetailEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        performDI()
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mPrefs.setKeyValue(PreferenceConstants.LANGUAGE_TYPE, mAppUtils.getLocaleLanguage())
    }


    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }


    override fun showLoading() {
        hideLoading()
        mProgressDialog = CommonUtil.initProgressDialog(this)

    }

    override fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.cancel()
        }
    }


    fun isInternetConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        if (netInfo != null && netInfo.isConnectedOrConnecting)
            return true
        else {
            Snackbar.make(findViewById<View>(android.R.id.content), "You are Offline!", 2000).show()
            return false
        }
    }


    fun getUserData(): SignInWithUserDetailEntity? {
        if (userDta == null) {
            try {
                userDta = Gson().fromJson(
                    mPrefs.getKeyValue(PreferenceConstants.USER_DATA),
                    SignInWithUserDetailEntity::class.java
                )
            } catch (e: Exception) {
                return null
            }
        }
        return userDta
    }

    fun startAnim() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    fun endAnim() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun setCustomDialog(isSuccess: Boolean, message: String, btn: String) {

//        val dialog = Dialog(this)
//        dialog.setContentView(R.layout.dialog_fail)
//        val width = ConstraintLayout.LayoutParams.MATCH_PARENT
//        val height = ConstraintLayout.LayoutParams.WRAP_CONTENT
//        dialog.window!!.setLayout(width, height)
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.findViewById<TextView>(R.id.mtvtittle).text=if (isSuccess) "Succuss" else "Fail"
//        dialog.findViewById<TextView>(R.id.mtvDiscription).text=message
//        dialog.findViewById<TextView>(R.id.buttonNo).text=btn
//        dialog.findViewById<TextView>(R.id.buttonNo).setOnClickListener { dialog.dismiss() }
//        dialog.show()

    }

    fun show404Error() {

    }

    override fun onError(resId: Int) {
        onError(getString(resId))
//        show404Error()
    }

    override fun showErrorMessage(message: String) {
        toast(message, false)
    }

    override fun showMessage(message: String) {
        if (true)
            toast(message, true)
        else
            toast(getString(R.string.some_error), false)
    }

    override fun showCustomMessage(message: String) {
        CommonUtil.showNoInternet(this, findViewById<View>(android.R.id.content), message)
    }

    override fun showMessage(resId: Int) {
        showMessage(getString(resId))
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }


    fun hideAllTypeKB(ibinder: IBinder?) {
        val imm = getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        imm.hideSoftInputFromWindow(ibinder!!, 0)
    }

    override fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }

    public fun hideKeyboardDialog(view: View? = null) {
        try {
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (view != null)
                imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    override fun openActivityOnTokenExpire() {
        //perform actions when groups expire
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name.startsWith(
                "android.webkit."
            )
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.left - scrcoords[0]
            val y = ev.rawY + view.top - scrcoords[1]
            if (x < view.left || x > view.right || y < view.top || y > view.bottom)
            //  mAppUtils.hideSoftKeyboard(window.decorView.rootView)
                hideKeyboard(window.decorView.rootView)
        }
        return super.dispatchTouchEvent(ev)
    }


    override fun onDestroy() {
        hideLoading()
        super.onDestroy()
    }

    private fun performDI() = AndroidInjection.inject(this)


    override fun enableButton() {

    }

    override fun onLogout(message: String) {
//        mAppUtils.showToast(message)
        if (message.equals("401")){
            Toast.makeText(this, "Your session has expired. Please sign in again.", Toast.LENGTH_SHORT).show()
            mPrefs.logoutUser()
            Intent(this, LoginEmailPasswordActivity::class.java).apply {
                startActivity(this)
            }
            finishAffinity()
            startAnim()
        }else if (message.equals("400")){
            Toast.makeText(this, "Email does not exist! Please Sign Up!", Toast.LENGTH_SHORT).show()
            mPrefs.logoutUser()
            Intent(this, PreSignUpCheckActivity::class.java).apply {
                startActivity(this)
            }
            finishAffinity()
            startAnim()
        }else{
            mPrefs.logoutUser()
            Intent(this, LoginEmailPasswordActivity::class.java).apply {
                startActivity(this)
            }
            finishAffinity()
            startAnim()
        }

    }

    override fun onError(message: String) {
        toast(message, false)
    }

    override fun disableButton() {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        endAnim()
    }

    // fused location
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        val locationRequest = LocationRequest.create()?.apply {
            interval = 1000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
//                    for (location in locationResult.locations) {
                    if (locationResult.locations.size > 0) {
                        val lat =
                            locationResult.locations[locationResult.locations.size - 1].latitude
                        val lng =
                            locationResult.locations[locationResult.locations.size - 1].longitude
                        mPrefs.setKeyValue(PreferenceConstants.LAT, lat.toString())
                        mPrefs.setKeyValue(PreferenceConstants.LNG, lng.toString())
                        //cal dis , longitude:
//                        Log.e("Distance ", distance(lat, lng, 21.0359851, 105.7804743).toString())
                    }
                    stopLocationUpdates()
                }
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
        task.addOnFailureListener { exception ->
            exception.printStackTrace()
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, 123)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.e("exception", sendEx.toString())
                }
            }
        }
    }


    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}
