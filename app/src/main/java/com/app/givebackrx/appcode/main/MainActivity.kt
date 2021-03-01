package com.app.givebackrx.appcode.main

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.Html
import android.util.DisplayMetrics
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.app.givebackrx.GBRxApp
import com.app.givebackrx.R
import com.app.givebackrx.appcode.main.charity.CharityFragment
import com.app.givebackrx.appcode.main.home.HomeFragment
import com.app.givebackrx.appcode.main.pharmacy.PharmacyFragment
import com.app.givebackrx.appcode.main.settings.SettingsFragment
import com.app.givebackrx.appcode.mycart.MyCartFragment
import com.app.givebackrx.appcode.orderSummary.shippingModule.ShippingFragment
import com.app.givebackrx.appcode.payment.PaymentFragment
import com.app.givebackrx.appcode.preSignUpModule.PreSignUpCheckActivity
import com.app.givebackrx.appcode.settings.dashboard.DashboardFragment
import com.app.givebackrx.base.BaseActivity
import com.app.givebackrx.utils.CommonUtil.permissionAlert
import com.app.givebackrx.utils.GlobalVariable
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.addMainFragment
import com.app.givebackrx.utils.extension.changeMainFragment
import com.app.givebackrx.utils.extension.replaceFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), HasSupportFragmentInjector {
    companion object {
        var canClose = true
    }

    val homeFragment: HomeFragment by lazy { HomeFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (permissionFile.checklocationPermissions(this)) {
            startLocationUpdates()
        }

        if (mPrefs.getKeyValue(PreferenceConstants.LANGUAGE).isEmpty()){
            mPrefs.setKeyValue(PreferenceConstants.LANGUAGE, "en")
        }
        logout.setOnClickListener { onLogout("") }
        if (GBRxApp.mSavePharmacyFavData!=null){
            changeMainFragment(homeContainer, homeFragment)
            addMainFragment(homeContainer, PharmacyFragment().apply { arguments=Bundle().apply { putString("fav_extra","true") } })
        }else {

            if (intent.hasExtra("signup")){
                showInformationPop("", "Your account has been created successfully! "+ mPrefs.getKeyValue(PreferenceConstants.CHARITY) +" has been selected as your default charity. You can update your selected charity at any time!")
            }
            if(intent.hasExtra("sixforone")){
                if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).toLowerCase().equals("portaluser")){
                    if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase().equals("silver")){
                        changeMainFragment(homeContainer, homeFragment)

                        val args = Bundle()
                        args.putString("sixforone", "true")
                        val paymentFragment = PaymentFragment()
                        paymentFragment.setArguments(args)

                        addMainFragment(homeContainer, paymentFragment)
                        bottomNavigationViewLogin.setSelectedItemId(R.id.menu_home);
                    }else{
                        changeMainFragment(homeContainer, DashboardFragment())
                        bottomNavigationViewLogin.setSelectedItemId(R.id.dash_home);
                    }

                }else{
                    changeMainFragment(homeContainer, homeFragment)
                }


            }else if(intent.hasExtra("page")){

                changeMainFragment(homeContainer, homeFragment)

                addMainFragment(homeContainer, MyCartFragment())
                bottomNavigationViewLogin.setSelectedItemId(R.id.menu_settings);

            }else{
                if (intent.hasExtra("login_extra")) {
                    changeMainFragment(homeContainer, DashboardFragment())
                    bottomNavigationViewLogin.setSelectedItemId(R.id.dash_home);
                } else
                    changeMainFragment(homeContainer, homeFragment)
            }

        }
    }

    var lastclicked = 0L
    override fun onStart() {
        super.onStart()
        if( mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN).not()){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mTvSix.text=Html.fromHtml("Get 6 Months of give"+ "<font><b>" + "back" + "</b></font>" + "Rx Gold Membership for only $5! USE CODE |" + "<font><b>" + " 6FOR1" + "</b></font>" , Html.FROM_HTML_MODE_COMPACT)
            } else {
                mTvSix.text=Html.fromHtml("Get 6 Months of give"+ "<font><b>" + "back" + "</b></font>" + "Rx Gold Membership for only $5! USE CODE |" + "<font><b>" + " 6FOR1" + "</b></font>")
            }
            mTvSix.visibility=View.VISIBLE
            bottomNavigationView.visibility=View.VISIBLE
        }else{
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).toLowerCase().equals("portaluser")){
                if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).equals("Silver")){

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mTvSix.text=Html.fromHtml("Get 6 Months of give"+ "<font><b>" + "back" + "</b></font>" + "Rx Gold Membership for only $5! USE CODE |" + "<font><b>" + " 6FOR1" + "</b></font>" , Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        mTvSix.text=Html.fromHtml("Get 6 Months of give"+ "<font><b>" + "back" + "</b></font>" + "Rx Gold Membership for only $5! USE CODE |" + "<font><b>" + " 6FOR1" + "</b></font>")
                    }
                    mTvSix.visibility=View.VISIBLE
                }else{
                    mTvSix.visibility=View.GONE
                }
            }else{
                mTvSix.visibility=View.GONE
            }



            bottomNavigationViewLogin.visibility=View.VISIBLE
        }


        mTvSix.setOnClickListener {
            if( mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN).not()){
                startActivity(Intent(this, PreSignUpCheckActivity::class.java).putExtra("sixforone","true"))
            }else{
                val args = Bundle()
                args.putString("sixforone", "true")
                val paymentFragment = PaymentFragment()
                paymentFragment.setArguments(args)

                addMainFragment(homeContainer, paymentFragment)

            }

        }

        
        bottomNavigationView.setOnNavigationItemSelectedListener {
            if (SystemClock.elapsedRealtime() - lastclicked > 500) {
//                if (it.itemId == bottomNavigationView.selectedItemId) return@setOnNavigationItemSelectedListener false
                when (it.itemId) {
                    R.id.menu_home -> {
                        changeMainFragment(homeContainer, homeFragment)
                    }
                    R.id.menu_charities -> {
                        val args = Bundle()
                        args.putString("from_dashboard", "no")
                        val charityFragment = CharityFragment()
                        charityFragment.setArguments(args)
                        changeMainFragment(homeContainer, charityFragment)
                    }
                    R.id.menu_settings -> changeMainFragment(homeContainer, SettingsFragment())
                    else -> changeMainFragment(homeContainer, HomeFragment())
                }
                lastclicked = SystemClock.elapsedRealtime()
                true
            } else {
                false
            }
        }



        bottomNavigationViewLogin.setOnNavigationItemSelectedListener {
            if (SystemClock.elapsedRealtime() - lastclicked > 500) {
//                if (it.itemId == bottomNavigationViewLogin.selectedItemId) return@setOnNavigationItemSelectedListener false
                GBRxApp.mSavePharmacyFavData=null
                when (it.itemId) {
                    R.id.menu_home -> changeMainFragment(homeContainer, homeFragment)
                    R.id.dash_home -> changeMainFragment(homeContainer, DashboardFragment())
                    R.id.menu_charities -> {

                        val args = Bundle()
                        args.putString("from_dashboard", "no")
                        val charityFragment = CharityFragment()
                        charityFragment.setArguments(args)
                        changeMainFragment(homeContainer, charityFragment)
                    }
                    R.id.menu_settings -> changeMainFragment(homeContainer, SettingsFragment())
                    else -> changeMainFragment(homeContainer, HomeFragment())
                }
                lastclicked = SystemClock.elapsedRealtime()
                true
            } else {
                false
            }
        }
    }

    @Inject
    internal lateinit var fragmentInjectr: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjectr
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GlobalVariable.REQUEST_CODE_LOCATION && permissions.size > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) permissionAlert(this, getString(R.string.location_permission_dialog))
            else startLocationUpdates()
        }
    }

//    fun addNewFragment(frag:Fragment){
//        supportFragmentManager.beginTransaction().addToBackStack(frag.tag)
//            .add(R.id.homeContainer, frag,frag.tag)
//            .commit()
//    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount>0)
        supportFragmentManager.popBackStackImmediate()
        else
        super.onBackPressed()
//        if (canClose) {
//            finishAffinity()
//        } else {
//            bottomNavigationView.menu.children.forEach {
//                when (it.itemId) {
//                    R.id.menu_home -> changeMainFragment(homeContainer, homeFragment)
//                    R.id.menu_charities -> changeMainFragment(homeContainer, HomeFragment())
//                    R.id.menu_settings -> changeMainFragment(homeContainer, HomeFragment())
//                    else -> changeMainFragment(homeContainer, HomeFragment())
//                }
//            }
//        }
    }

    val mInfoBuilder: Dialog by lazy { Dialog(this) }
    fun showInformationPop(ttl: String = "", desc: String) {
        mInfoBuilder.setContentView(R.layout.charitysignee_info_dialog);
        val displayMetrics = DisplayMetrics()
        this.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        mInfoBuilder.window!!.setBackgroundDrawableResource(R.drawable.shadow_border_bg_more_rounded)
        mInfoBuilder.window!!.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
            (displayMetrics.widthPixels * 0.99).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mInfoBuilder.findViewById<TextView>(R.id.titleHeader).visibility=View.GONE
        mInfoBuilder.findViewById<TextView>(R.id.tvDesc).text = desc

        mInfoBuilder.findViewById<TextView>(R.id.tvDone)
            .setOnClickListener {
                mInfoBuilder.dismiss()
            }
        mInfoBuilder.show();
    }
}
