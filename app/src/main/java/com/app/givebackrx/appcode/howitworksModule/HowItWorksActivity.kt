package com.app.givebackrx.appcode.howitworksModule

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.ViewPager
import com.afollestad.viewpagerdots.DotsIndicator
import com.app.givebackrx.R
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.loginEmailPaswordModule.LoginEmailPasswordActivity
import com.app.givebackrx.base.BaseActivity
import com.app.givebackrx.utils.PreferenceConstants
import kotlinx.android.synthetic.main.activity_how_it_works.*


class HowItWorksActivity : BaseActivity() {

    private val draw: ArrayList<Drawable> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_it_works)
//        if (mPrefs.getKeyValue(PreferenceConstants.HOWITWORK) == "1"){
                startActivity(Intent(this, MainActivity::class.java))
//        }

        draw.add(ResourcesCompat.getDrawable(resources, R.drawable.ic_1, null)!!)
        draw.add(ResourcesCompat.getDrawable(resources, R.drawable.ic_2, null)!!)
        draw.add(ResourcesCompat.getDrawable(resources, R.drawable.ic_3, null)!!)

        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        val dot = findViewById<DotsIndicator>(R.id.dots)
        viewPager.setAdapter(ViewPagerAdapter(this, draw))
        dot.attachViewPager(viewPager)

        tvskip.setOnClickListener {
            mPrefs.setKeyValue(PreferenceConstants.HOWITWORK,"0")
                startActivity(Intent(this, MainActivity::class.java))
        }

        tvHide.setOnClickListener {
            mPrefs.setKeyValue(PreferenceConstants.HOWITWORK,"1")
                startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onGeneratedToken(lastAction: String) {

    }
}
