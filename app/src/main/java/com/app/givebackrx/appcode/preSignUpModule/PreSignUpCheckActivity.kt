package com.app.givebackrx.appcode.preSignUpModule

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputFilter
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Patterns
import android.view.View
import android.widget.TextView
import com.app.givebackrx.R
import com.app.givebackrx.appcode.loginEmailModule.LoginEmailActivity
import com.app.givebackrx.appcode.loginEmailPaswordModule.LoginEmailPasswordActivity
import com.app.givebackrx.appcode.presignuphone.PreSignupPhoneActivity
import com.app.givebackrx.appcode.signupModule.SignUpActivity
import com.app.givebackrx.appcode.web.WebActivity
import com.app.givebackrx.base.BaseActivity
import com.app.givebackrx.utils.extension.makeToast
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_pre_sign_up_check.*
import kotlinx.android.synthetic.main.activity_pre_sign_up_check.btNext
import kotlinx.android.synthetic.main.activity_pre_sign_up_check.checkboxhippa
import kotlinx.android.synthetic.main.activity_pre_sign_up_check.checkboxterms
import kotlinx.android.synthetic.main.activity_pre_sign_up_check.etEmail
import kotlinx.android.synthetic.main.activity_pre_sign_up_check.etPhoneNumber
import kotlinx.android.synthetic.main.activity_pre_sign_up_check.ivBack
import kotlinx.android.synthetic.main.activity_pre_sign_up_check.mTvHippa
import kotlinx.android.synthetic.main.activity_pre_sign_up_check.mTvPrivacy
import kotlinx.android.synthetic.main.activity_pre_sign_up_check.mTvTerms
import kotlinx.android.synthetic.main.activity_pre_sign_up_check.textView8
import kotlinx.android.synthetic.main.activity_pre_sign_up_check.tvEmail
import kotlinx.android.synthetic.main.activity_pre_sign_up_check.tvSignIn
import kotlinx.android.synthetic.main.activity_pre_signup_phone.*
import javax.inject.Inject

class PreSignUpCheckActivity : BaseActivity(), View.OnClickListener, IPreSignupCheckView {
    @Inject
    lateinit var presenter: PreSignupCheckPresenter<IPreSignupCheckView>

    var screentype = "email"
    lateinit var textView3: TextView
    lateinit var textView6: TextView
    lateinit var textView4: TextView
    lateinit var textView2: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_sign_up_check)
        presenter.onAttach(this)
        ivBack.setOnClickListener(this)
        tvEmail.setOnClickListener(this)
        btNext.setOnClickListener(this)
        tvSignIn.setOnClickListener(this)
        mTvTerms.setOnClickListener(this)
        mTvPrivacy.setOnClickListener(this)
        mTvHippa.setOnClickListener(this)
        textView3 = findViewById<TextView>(R.id.textView3)
        textView6 = findViewById<TextView>(R.id.textView6)
        textView4 = findViewById<TextView>(R.id.textView4)
        textView2 = findViewById<TextView>(R.id.textView2)

        etPhoneNumber.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            if (source.length > 0) {
                if (!Character.isDigit(source[0])) return@InputFilter "" else {
                    if (dstart == 0) {
                        return@InputFilter "($source"
                    } else if (dstart == 3) {
                        return@InputFilter "$source) "
                    } else if (dstart == 9) return@InputFilter "-$source"
                    else if (dstart >= 14) return@InputFilter ""
                }
            }
            null
        })
        textView8.apply {
            text = SpannableString(this.text.toString()).also {
                    val click = object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            startActivity(Intent(this@PreSignUpCheckActivity, WebActivity::class.java))
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
            R.id.tvEmail -> {
                startActivity(Intent(this@PreSignUpCheckActivity,PreSignupPhoneActivity::class.java))
//                if (screentype.equals("email")) {
//                    screentype = "phone"
//                    tvEmail.setText(" Phone Number")
//                    textView2.setText("Enter your Phone Number to receive OTP")
//                    textView4.visibility = View.VISIBLE
//                    etEmail.visibility = View.VISIBLE
//                    textView3.visibility = View.GONE
//                    etPhoneNumber.visibility = View.GONE
//                    textView6.visibility = View.GONE
//                } else {
//                    screentype = "email"
//                    tvEmail.setText(" Email")
//                    textView2.setText("Enter your Email Address to receive OTP")
//                    textView4.visibility = View.GONE
//                    etEmail.visibility = View.GONE
//                    textView3.visibility = View.VISIBLE
//                    etPhoneNumber.visibility = View.VISIBLE
//                    textView6.visibility = View.VISIBLE
//
//                }
            }

            R.id.btNext -> {
//                startActivity(Intent(this, SignUpActivity::class.java))
                if (screentype == "phone") {
                    if (etPhoneNumber.text.toString().trim().isEmpty()) {

                    } else if (etPhoneNumber.text.toString().trim().length < 14) {

                    }  else if (!checkboxterms.isChecked) {
                        showMessage(getString(R.string.check_services_terms))
                    } else if (!checkboxhippa.isChecked) {
                        showMessage(getString(R.string.check_hippa))
                    }else {
//                        if (isInternetConnected()) {
//                            JsonObject().apply {
//                                addProperty("phone",etPhoneNumber.text.toString().trim())
//                                addProperty("type","phone")
//                                presenter.preSignUpVerification(this)
//                            }
//                        }
                    }
                } else {
                    if (etEmail.text.toString().trim().isEmpty()) {
                        showMessage(getString(R.string.enter_email_address))
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString().trim())
                            .matches()
                    ) {
                        showMessage(getString(R.string.enter_valid_address))
                    } else if (!checkboxterms.isChecked) {
                        showMessage(getString(R.string.check_services_terms))
                    } else if (!checkboxhippa.isChecked) {
                        showMessage(getString(R.string.check_hippa))
                    } else {
                        if (isInternetConnected()) {
                            JsonObject().apply {
                                addProperty("email", etEmail.text.toString().trim())
                                addProperty("type", "email")
                                presenter.preSignUpVerification(this)
                            }
                        }
                    }
                }
            }

            R.id.tvSignIn -> {
                if (intent.hasExtra("sixforone")){
                    startActivity(Intent(this, LoginEmailPasswordActivity::class.java).putExtra("sixforone","true"))
                }else{
                    startActivity(Intent(this, LoginEmailPasswordActivity::class.java))
                }
            }

          R.id.mTvTerms -> {
              startActivity(Intent(this@PreSignUpCheckActivity, WebActivity::class.java).putExtra("name","Terms of Service").putExtra("url","https://www.givebackrx.com/terms_and_condition"))
            }

          R.id.mTvPrivacy -> {
              startActivity(Intent(this@PreSignUpCheckActivity, WebActivity::class.java).putExtra("name","Privacy Policy").putExtra("url","https://www.givebackrx.com/privacyPolicy"))
            }

          R.id.mTvHippa -> {
              startActivity(Intent(this@PreSignUpCheckActivity, WebActivity::class.java).putExtra("name","HIPAA Privacy Policy").putExtra("url","https://www.givebackrx.com/hippa_nopp"))
            }

        }
    }


    override fun onPreSignUpVerification(canRegister: Boolean) {
        if (canRegister) {

            if (intent.hasExtra("sixforone")){

                startActivity(Intent(this@PreSignUpCheckActivity, SignUpActivity::class.java).putExtra("email_extra", etEmail.text.toString().trim()).putExtra("sixforone","true"))


            }else if (intent.hasExtra("page")){

                startActivity(Intent(this@PreSignUpCheckActivity, SignUpActivity::class.java).putExtra("email_extra", etEmail.text.toString().trim()).putExtra("page","cart"))


            }else{
                startActivity(
                    Intent(
                        this@PreSignUpCheckActivity,
                        SignUpActivity::class.java
                    ).putExtra("email_extra", etEmail.text.toString().trim())
                )
            }



        } else {

            if (intent.hasExtra("sixforone")){
                startActivity(
                    Intent(
                        this@PreSignUpCheckActivity,
                        LoginEmailPasswordActivity::class.java
                    ).putExtra("email_extra", etEmail.text.toString().trim()).putExtra("sixforone","true"))
            }else{
                startActivity(
                    Intent(
                        this@PreSignUpCheckActivity,
                        LoginEmailPasswordActivity::class.java
                    ).putExtra("email_extra", etEmail.text.toString().trim())
                )
            }

        }
    }

    override fun onGeneratedToken(lastAction: String) {

    }
}
