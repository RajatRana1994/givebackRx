package com.app.givebackrx.appcode.verifyOtpModule

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import com.app.givebackrx.R
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.signupModule.SignUpActivity
import com.app.givebackrx.base.BaseActivity
import com.app.givebackrx.data.entity.SignInWithUserDetailData
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_pre_sign_up_check.*
import kotlinx.android.synthetic.main.activity_verify_otp.*
import kotlinx.android.synthetic.main.activity_verify_otp.ivBack
import kotlinx.android.synthetic.main.activity_verify_otp.textView2
import kotlinx.android.synthetic.main.activity_verify_otp.tvEmail
import javax.inject.Inject

class VerifyOTPActivity : BaseActivity(), IVerifyOTPView {

    @Inject
    lateinit var presenter: VerifyOTPPresenter<IVerifyOTPView>

    val value_extra: String by lazy { intent.getStringExtra("value_extra") ?: "" }
    val type_extra: String by lazy { intent.getStringExtra("type_extra") ?: "" }
    val page_extra: String by lazy { intent.getStringExtra("page_extra") ?: "signin" }
    val otp_extra: String by lazy { intent.getStringExtra("otp_extra") ?: "" }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)
        presenter.onAttach(this)
        tvEmail.text = value_extra

        val desc = "<font color=#363636>Didn't receive a code?  </font><font color=#DA354E><u><b>Resend</b></u></font>"

        textView2.text= if (type_extra.contains("email"))"Enter the 6-digit code from the email we just sent to" else "Enter the 6-digit code from the phone number we just sent to"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvResend.setText(Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY))
        } else {
            tvResend.setText(Html.fromHtml(desc))
        }

        ivBack.setOnClickListener { finish() }
        btVerify.setOnClickListener {
//            if (otp_extra.equals(firstPinView.text.toString())) {
                if (isInternetConnected())
                    JsonObject().apply {
                        addProperty("page",page_extra)
                        addProperty("type",type_extra)
                        addProperty("value",value_extra)
                        addProperty("otp",firstPinView.text.toString())
                        presenter.verifyOTP(this)
                    }
//            } else toast("Wrong OTP", false)
        }

        tvResend.setOnClickListener {
                if (isInternetConnected())
                    JsonObject().apply {
                        addProperty("page",page_extra)
                        addProperty("type",type_extra)
                        addProperty("value",value_extra)
                        presenter.sendAgain(this)
                    }

        }
    }

    override fun onVerifyOTPResponse(it: SignInWithUserDetailData) {
        if (it.user_type.equals("portalUser")){

        mPrefs.setUserLoggedIn(PreferenceConstants.USER_LOGGED_IN, true)
        mPrefs.setKeyValue(PreferenceConstants.USER_TYPE, it.user_type ?: "")
        mPrefs.setKeyValue(PreferenceConstants.REFERRAL, it.referral_code ?: "")
        mPrefs.setKeyValue(PreferenceConstants.MEMBER_TYPE, it.membertype ?: "")
        mPrefs.setKeyValue(PreferenceConstants.ACCESSTOKEN, it.jwttoken ?: "")
        mPrefs.setKeyValue(PreferenceConstants.USER_NAME, "${it.firstname} ${it.lastname}")
        mPrefs.setKeyValue(PreferenceConstants.CHARITY, it.selected_charity_name?:"")
        mPrefs.setKeyValue(
            PreferenceConstants.USER_DATA,
            Gson().toJson(it)
        )

        if(intent.hasExtra("signup")){
            if (intent.hasExtra("sixforone")){
                startActivity(Intent(this@VerifyOTPActivity,
                    MainActivity::class.java).putExtra("login_extra","true").putExtra("sixforone","true").putExtra("signup","true") )
            }else if (intent.hasExtra("page")){
                startActivity(Intent(this@VerifyOTPActivity,
                    MainActivity::class.java).putExtra("login_extra","true").putExtra("page","cart").putExtra("signup","true"))
            }else{
                startActivity(Intent(this@VerifyOTPActivity,
                    MainActivity::class.java).putExtra("login_extra","true").putExtra("signup","true"))
            }
        }else{
            if (intent.hasExtra("sixforone")){
                startActivity(Intent(this@VerifyOTPActivity,
                    MainActivity::class.java).putExtra("login_extra","true").putExtra("sixforone","true") )
            }else if (intent.hasExtra("page")){
                startActivity(Intent(this@VerifyOTPActivity,
                    MainActivity::class.java).putExtra("login_extra","true").putExtra("page","cart"))
            }else{
                startActivity(Intent(this@VerifyOTPActivity,
                    MainActivity::class.java).putExtra("login_extra","true"))
            }
        }
        finishAffinity()
        }else{

            toast("Charity user is coming soon on Android app", false)
        }
    }

    override fun onGeneratedToken(lastAction: String) {

    }
}
