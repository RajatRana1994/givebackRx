package com.app.givebackrx.appcode.forgot

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.view.View
import com.app.givebackrx.R
import com.app.givebackrx.appcode.preSignUpModule.PreSignUpCheckActivity
import com.app.givebackrx.base.BaseActivity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.utils.extension.isInternetAvailable
import com.app.givebackrx.utils.extension.toast
import kotlinx.android.synthetic.main.activity_forgot_pass.*
import javax.inject.Inject

class ForgotPassActivity : BaseActivity(), View.OnClickListener,IForgotPassView {

    @Inject
    lateinit var presenter: ForgotPassPresenter<IForgotPassView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        presenter.onAttach(this)
        ivBack.setOnClickListener(this)
        textSignin.setOnClickListener(this)
        tvSignUp.setOnClickListener(this)
        btNext.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when (p0) {
            ivBack -> onBackPressed()
            textSignin -> onBackPressed()
            tvSignUp -> {
                startActivity(Intent(this@ForgotPassActivity, PreSignUpCheckActivity::class.java))
                finish()
            }
            btNext -> {
                if (etEmail.text.toString().trim().isEmpty()) {
                    toast(getString(R.string.enteremail), false)
                } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString().trim())
                        .matches()
                ) {
                    toast(getString(R.string.entervalidemail), false)
                } else {
                    if (isInternetAvailable()){
                        presenter.forgotPassword(etEmail.text.toString())
                    }
                }
            }

        }
    }

    override fun onForgotPassResp(it: SignInWithUserDetailEntity) {
        toast(it.message,true)
        Handler().postDelayed({onBackPressed()},1000)
    }


    override fun onGeneratedToken(lastAction: String) {

    }
}