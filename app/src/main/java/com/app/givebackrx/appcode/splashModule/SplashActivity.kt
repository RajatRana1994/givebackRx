package com.app.givebackrx.appcode.splashModule

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.app.givebackrx.R
import com.app.givebackrx.appcode.howitworksModule.HowItWorksActivity
import com.app.givebackrx.appcode.main.MainActivity

class SplashActivity : AppCompatActivity() {

    var SPLASH_TIME_OUT = 2000

    val handler by lazy { Handler() }
    val runnable by lazy {
        Runnable {
            startActivity(Intent(this, MainActivity::class.java))
//            startActivity(Intent(this, HowItWorksActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        handler.postDelayed(runnable, SPLASH_TIME_OUT.toLong())
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}
