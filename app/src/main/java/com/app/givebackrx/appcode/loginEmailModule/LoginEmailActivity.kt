package com.app.givebackrx.appcode.loginEmailModule

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Patterns
import android.view.View
import com.app.givebackrx.R
import com.app.givebackrx.appcode.loginEmailPaswordModule.LoginEmailPasswordActivity
import com.app.givebackrx.appcode.loginPhoneNumberModule.LoginPhoneActivity
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.preSignUpModule.PreSignUpCheckActivity
import com.app.givebackrx.appcode.verifyOtpModule.VerifyOTPActivity
import com.app.givebackrx.appcode.web.WebActivity
import com.app.givebackrx.base.BaseActivity
import com.app.givebackrx.data.entity.SignInWithUserDetailData
import com.app.givebackrx.utils.AppConstants.PATTERN
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_login_email_or_phone.*
import java.util.*
import javax.inject.Inject

class LoginEmailActivity : BaseActivity(), View.OnClickListener, ILoginEmailView {

    @Inject
    lateinit var presenter: LoginEmailPresenter<ILoginEmailView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_email_or_phone)
        presenter.onAttach(this)
        ivBack.setOnClickListener(this)
        tvPhoneNumber.setOnClickListener(this)
        btNext.setOnClickListener(this)
        tvSignUp.setOnClickListener(this)
        tvPassword.setOnClickListener(this)
        textView8.apply {
            textView8.text = SpannableString(this.text.toString()).also {
                val click = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        startActivity(Intent(this@LoginEmailActivity, WebActivity::class.java))
                    }
                }
                it.setSpan(StyleSpan(Typeface.BOLD), 32, 36, 0)
                it.setSpan(ForegroundColorSpan(Color.BLUE), 41, textView8.text.length, 0)
                it.setSpan(click, 41, textView8.text.length, 0)
            }
            setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivBack -> finish()
            R.id.tvPhoneNumber ->{
                if (intent.hasExtra("sixforone")){
                    startActivity(Intent(this, LoginPhoneActivity::class.java).putExtra("sixforone","true"))
                }else if (intent.hasExtra("page")){
                    startActivity(Intent(this, LoginPhoneActivity::class.java).putExtra("page","cart"))
                }else{
                    startActivity(Intent(this, LoginPhoneActivity::class.java))
                }
            }
            R.id.btNext -> {
                loginEmail()
            }
            R.id.tvSignUp ->{
                if (intent.hasExtra("sixforone")){
                    startActivity(Intent(this, PreSignUpCheckActivity::class.java).putExtra("sixforone","true"))
                } else if (intent.hasExtra("page")){
                    startActivity(Intent(this, PreSignUpCheckActivity::class.java).putExtra("page","cart"))
                }else{
                    startActivity(Intent(this, PreSignUpCheckActivity::class.java))
                }
            }
            R.id.tvPassword -> {
                if (intent.hasExtra("sixforone")){
                    startActivity(Intent(this, LoginEmailPasswordActivity::class.java).putExtra("sixforone","true"))
                }else if (intent.hasExtra("page")){
                    startActivity(Intent(this, LoginEmailPasswordActivity::class.java).putExtra("page","cart"))
                }else{
                    startActivity(Intent(this, LoginEmailPasswordActivity::class.java))
                }
            }
        }
    }

    fun loginEmail(){
        if (etEmail.text.toString().trim().isEmpty()) {
            toast(getString(R.string.email_address), false)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString().trim()).matches()) {
            toast(getString(R.string.enter_valid_address), false)
        } else if (etPassword.visibility == View.VISIBLE && etPassword.text.toString().trim().isEmpty()) {
            toast(getString(R.string.enter_password), false)
        } else if (etPassword.visibility == View.VISIBLE && !PATTERN.matcher(etPassword.text.toString().trim()).matches()) {
            toast(getString(R.string.password_length_message), false)
        } else {
            if (isInternetConnected())
                JsonObject().apply {
                    addProperty("value", etEmail.text.toString().trim())
                    if (etPassword.visibility == View.VISIBLE)
                        addProperty("password", etPassword.text.toString().trim())
                    addProperty("type", "email")
                    presenter.signIn(this)
                }
        }
    }


    override fun onSignInWithUserDetail(data: SignInWithUserDetailData) {
        if (data.two_factor_auth != null) {
            when {
                data.two_factor_auth -> {
                    if (etPassword.visibility == View.VISIBLE) {

                        if (intent.hasExtra("sixforone")){
                            Intent(this@LoginEmailActivity, VerifyOTPActivity::class.java).apply {
                                putExtra("value_extra", etEmail.text.toString().trim())
                                putExtra("type_extra", "email")
                                putExtra("page_extra", if (data.user_type!!.toLowerCase(Locale.ENGLISH).equals("charityuser")) "charitySignIn" else "signIn")
                                putExtra("otp_extra", data.otp.toString())
                                putExtra("sixforone","true")
                                startActivity(this)
                            }
                        }else if (intent.hasExtra("page")){
                            Intent(this@LoginEmailActivity, VerifyOTPActivity::class.java).apply {
                                putExtra("value_extra", etEmail.text.toString().trim())
                                putExtra("type_extra", "email")
                                putExtra("page_extra", if (data.user_type!!.toLowerCase(Locale.ENGLISH).equals("charityuser")) "charitySignIn" else "signIn")
                                putExtra("otp_extra", data.otp.toString())
                                putExtra("page", "cart")
                                startActivity(this)
                            }
                        }else{
                            Intent(this@LoginEmailActivity, VerifyOTPActivity::class.java).apply {
                                putExtra("value_extra", etEmail.text.toString().trim())
                                putExtra("type_extra", "email")
                                putExtra("page_extra", if (data.user_type!!.toLowerCase(Locale.ENGLISH).equals("charityuser")) "charitySignIn" else "signIn")
                                putExtra("otp_extra", data.otp.toString())
                                startActivity(this)
                            }
                        }
                    } else {
                        textView4.visibility = View.VISIBLE
                        etPassword.visibility = View.VISIBLE
                    }
                }
                else -> {
                    if (intent.hasExtra("sixforone")){
                       Intent(this@LoginEmailActivity, VerifyOTPActivity::class.java).apply {
                            putExtra("value_extra", etEmail.text.toString().trim())
                            putExtra("type_extra", "email")
                            putExtra("page_extra", if (data.user_type!!.toLowerCase(Locale.ENGLISH).equals("charityuser")) "charitySignIn" else "signIn")
                            putExtra("otp_extra", data.otp.toString())
                            putExtra("sixforone","true")
                            startActivity(this)
                        }

                    }else if (intent.hasExtra("page")){
                       Intent(this@LoginEmailActivity, VerifyOTPActivity::class.java).apply {
                            putExtra("value_extra", etEmail.text.toString().trim())
                            putExtra("type_extra", "email")
                            putExtra("page_extra", if (data.user_type!!.toLowerCase(Locale.ENGLISH).equals("charityuser")) "charitySignIn" else "signIn")
                            putExtra("otp_extra", data.otp.toString())
                            putExtra("page", "cart")
                            startActivity(this)
                        }

                    }else{
                        Intent(this@LoginEmailActivity, VerifyOTPActivity::class.java).apply {
                            putExtra("value_extra", etEmail.text.toString().trim())
                            putExtra("type_extra", "email")
                            putExtra("page_extra", if (data.user_type!!.toLowerCase(Locale.ENGLISH).equals("charityuser")) "charitySignIn" else "signIn")
                            putExtra("otp_extra", data.otp.toString())
                            startActivity(this)
                        }
                    }

                }
            }
        } else {
            mPrefs.setUserLoggedIn(PreferenceConstants.USER_LOGGED_IN, true)
            mPrefs.setKeyValue(PreferenceConstants.USER_TYPE, data.user_type ?: "")
            mPrefs.setKeyValue(PreferenceConstants.MEMBER_TYPE, data.membertype ?: "")
            mPrefs.setKeyValue(PreferenceConstants.REFERRAL, data.referral_code ?: "")
            mPrefs.setKeyValue(PreferenceConstants.ACCESSTOKEN, data.jwttoken ?: "")
            mPrefs.setKeyValue(PreferenceConstants.USER_NAME, "${data.firstname} ${data.lastname}")
            mPrefs.setKeyValue(PreferenceConstants.USER_DATA, Gson().toJson(data))
            toast("Logged in", true)

            if (intent.hasExtra("sixforone")){
                startActivity(Intent(this@LoginEmailActivity, MainActivity::class.java).putExtra("login_extra","true").putExtra("sixforone","true"))
            }else if (intent.hasExtra("sixforone")){
                startActivity(Intent(this@LoginEmailActivity, MainActivity::class.java).putExtra("login_extra","true").putExtra("page","cart"))
            }else{
                startActivity(
                        Intent(
                                this@LoginEmailActivity,
                                MainActivity::class.java
                        ).putExtra("login_extra","true")
                )
            }
            finishAffinity()
        }
    }

    override fun onGeneratedToken(lastAction: String) {

    }
}