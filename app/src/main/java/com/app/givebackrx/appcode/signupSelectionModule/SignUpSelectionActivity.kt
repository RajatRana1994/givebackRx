package com.app.givebackrx.appcode.signupSelectionModule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.app.givebackrx.R
import com.app.givebackrx.appcode.charitysignee.CharitySigneeActivity
import com.app.givebackrx.appcode.preSignUpModule.PreSignUpCheckActivity
import kotlinx.android.synthetic.main.activity_sign_up_selection.*

class SignUpSelectionActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0) {
            ivBack -> {
                finish()
            }
            mIvUser -> {
                startActivity(Intent(this@SignUpSelectionActivity, PreSignUpCheckActivity::class.java))
            }
            mTvUser -> {
                startActivity(Intent(this@SignUpSelectionActivity, PreSignUpCheckActivity::class.java))
            }
            mIvCharity -> {
                startActivity(Intent(this@SignUpSelectionActivity, CharitySigneeActivity::class.java))
            }
            mTvCharity -> {
                startActivity(Intent(this@SignUpSelectionActivity, CharitySigneeActivity::class.java))
            }

            mIvPartner -> {
                startActivity(Intent(this@SignUpSelectionActivity, PreSignUpCheckActivity::class.java))
            }

            mTvPartner -> {
                startActivity(Intent(this@SignUpSelectionActivity, PreSignUpCheckActivity::class.java))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_sign_up_selection)
        ivBack.setOnClickListener(this)
        mIvUser.setOnClickListener(this)
        mTvUser.setOnClickListener(this)
        mIvCharity.setOnClickListener(this)
        mTvCharity.setOnClickListener(this)
        mIvPartner.setOnClickListener(this)
        mTvPartner.setOnClickListener(this)
    }
}
