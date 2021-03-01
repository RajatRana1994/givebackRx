package com.app.givebackrx.appcode.main.settings

import android.app.AlertDialog
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.TextView
import com.app.givebackrx.R
import com.app.givebackrx.appcode.feedback.FeedbackFragment
import com.app.givebackrx.appcode.loginEmailPaswordModule.LoginEmailPasswordActivity
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.main.pharmacy.ViewAnimationUtils
import com.app.givebackrx.appcode.main.settings.myNoticesModule.MyNoticesFragment
import com.app.givebackrx.appcode.main.settings.myTasksModule.MyTaskFragment
import com.app.givebackrx.appcode.main.settings.referralsModule.ReferralsFragment
import com.app.givebackrx.appcode.main.settings.updateCardModule.UpdateCardFragment
import com.app.givebackrx.appcode.mycharitydetail.MyCharityDetailFragment
import com.app.givebackrx.appcode.preSignUpModule.PreSignUpCheckActivity
import com.app.givebackrx.appcode.profileModule.ProfileFragment
import com.app.givebackrx.appcode.settings.changePasswordModule.ChangePasswordFragment
import com.app.givebackrx.appcode.settings.dashboard.DashboardFragment
import com.app.givebackrx.appcode.settings.favouriteModule.FavouritesFragment
import com.app.givebackrx.appcode.settings.help.HelpFragment
import com.app.givebackrx.appcode.settings.help.HelpTicketFragment
import com.app.givebackrx.appcode.settings.mycharity.MyCharityFragment
import com.app.givebackrx.appcode.settings.nearme.NearMeFragment
import com.app.givebackrx.appcode.settings.notificationModule.NotificationsFragment
import com.app.givebackrx.appcode.settings.purchase.RecentPurchaseFragment
import com.app.givebackrx.appcode.settings.securityModule.SecurityFragment
import com.app.givebackrx.appcode.store.StoreFragment
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.AllinOneDialog
import com.app.givebackrx.utils.extension.addMainFragment
import com.app.givebackrx.utils.extension.loadImageRadius
import com.app.givebackrx.utils.extension.onOffVisibility
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.language_selection_popup.*

class SettingsFragment : BaseFragment(), View.OnClickListener {

    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun setUp(view: View) {
        setClicks()
        ivUserImage.onOffVisibility(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
        tvLogin.onOffVisibility(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN).not())
        tvSignup.onOffVisibility(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN).not())
//        tvDashboard.onOffVisibility(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
        tvAboutMe.onOffVisibility(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
        tvSecurity.onOffVisibility(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
//        tvShareFeedback.onOffVisibility(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
        tvLogout.onOffVisibility(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
            ivUserImage.loadImageRadius(mAppUtils.getUserData(mPrefs)!!.profile_pic ?: "", 100)
        tvShareApp.onOffVisibility(false)
        tvGbrxStore.onOffVisibility(true)

        if (mPrefs.getKeyValue(PreferenceConstants.LANGUAGE).equals("en")){
            tvLanguage.text="English"
        }else{
            tvLanguage.text="Spanish"
        }
    }

    private fun setClicks() {
        tvLogin.setOnClickListener(this)
        tvSignup.setOnClickListener(this)
        tvDashboard.setOnClickListener(this)
        tvAboutMe.setOnClickListener(this)
        tvNearMe.setOnClickListener(this)
        tvSecurity.setOnClickListener(this)
        tvChangePassword.setOnClickListener(this)
        tvNotification.setOnClickListener(this)
        tvMyProfile.setOnClickListener(this)
        ivUserImage.setOnClickListener(this)
        tvSecure.setOnClickListener(this)
        tvFavMedications.setOnClickListener(this)
        tvRecentPurchases.setOnClickListener(this)
        tvUpdateCard.setOnClickListener(this)
        tvUpdateCharity.setOnClickListener(this)
        tvMyTasks.setOnClickListener(this)
        tvMyReferrals.setOnClickListener(this)
        tvMyNotices.setOnClickListener(this)
        tvShareApp.setOnClickListener(this)
        tvShareViaMsg.setOnClickListener(this)
        tvShareViaInsta.setOnClickListener(this)
        tvShareViaFacebook.setOnClickListener(this)
        tvShareFeedback.setOnClickListener(this)
        tvGetHelp.setOnClickListener(this)
        tvHelpTicket.setOnClickListener(this)
        tvLogout.setOnClickListener(this)
        tvGbrxStore.setOnClickListener(this)
        tvLanguage.setOnClickListener(this)
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

    companion object {

    }

    override fun onClick(p0: View) {
        when (p0) {
            tvLanguage -> {
                showLanguagePop()

            }
            tvLogin -> startActivity(
                Intent(
                    requireActivity(),
                    LoginEmailPasswordActivity::class.java
                )
            )
            tvSignup -> startActivity(Intent(requireActivity(), PreSignUpCheckActivity::class.java))
            tvGbrxStore -> mainActivity.addMainFragment(R.id.homeContainer, StoreFragment())
            tvDashboard -> mainActivity.addMainFragment(R.id.homeContainer, DashboardFragment())
            tvAboutMe -> {
                if (groutAboutMe.visibility == View.GONE) {
                    tvAboutMe.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_my_profile_on,
                        0,
                        R.drawable.ic_up_arrow,
                        0
                    )
                    ViewAnimationUtils.expand(groutAboutMe)
                    tvUpdateCard.onOffVisibility(
                        mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).contains("charity").not()
                    )
//                    tvRecentPurchases.onOffVisibility(
//                        mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).contains("charity").not()
//                    )
//                    tvFavMedications.onOffVisibility(
//                        mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).contains("charity").not()
//                    )

                    tvRecentPurchases.onOffVisibility(
                        true
                    )
                    tvFavMedications.onOffVisibility(
                       true)

                } else {
                    tvAboutMe.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_my_profile_on,
                        0,
                        R.drawable.ic_down_arrow,
                        0
                    )
                    groutAboutMe.onOffVisibility(false)
                    tvUpdateCard.onOffVisibility(false)
                    tvRecentPurchases.onOffVisibility(false)
                    tvFavMedications.onOffVisibility(false)
                }
                tvShareApp.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_share,
                    0,
                    R.drawable.ic_down_arrow,
                    0
                )
                groupShare.onOffVisibility(false)
            }
            tvSecurity -> {
                if (groupSecurity.visibility == View.GONE) {
                    tvSecurity.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_favorite_medications_on,
                        0,
                        R.drawable.ic_up_arrow,
                        0
                    )
                    ViewAnimationUtils.expand(groupSecurity)
                } else {
                    tvSecurity.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_favorite_medications_on,
                        0,
                        R.drawable.ic_down_arrow,
                        0
                    )
                    groupSecurity.onOffVisibility(false)
                }
                tvShareApp.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_share,
                    0,
                    R.drawable.ic_down_arrow,
                    0
                )
                groupShare.onOffVisibility(false)
            }
            tvFavMedications -> {
                mainActivity.addMainFragment(R.id.homeContainer, FavouritesFragment())
            }
            tvRecentPurchases -> {
                mainActivity.addMainFragment(R.id.homeContainer, RecentPurchaseFragment())
            }
            tvUpdateCard -> {
                mainActivity.addMainFragment(R.id.homeContainer, UpdateCardFragment())
            }
            tvUpdateCharity -> {
                if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).contains("charity"))
                    mainActivity.addMainFragment(R.id.homeContainer, MyCharityDetailFragment())
                else
                    mainActivity.addMainFragment(R.id.homeContainer, MyCharityFragment())
            }
            tvMyTasks -> {
                mainActivity.addMainFragment(R.id.homeContainer, MyTaskFragment())
            }
            tvMyReferrals -> {
                mainActivity.addMainFragment(R.id.homeContainer, ReferralsFragment())
            }
            tvMyNotices -> {
                mainActivity.addMainFragment(R.id.homeContainer, MyNoticesFragment())
            }
            tvNearMe -> {
                mainActivity.addMainFragment(R.id.homeContainer, NearMeFragment())
            }
            tvChangePassword -> {
                mainActivity.addMainFragment(R.id.homeContainer, ChangePasswordFragment())
            }
            tvNotification -> {
                mainActivity.addMainFragment(R.id.homeContainer, NotificationsFragment())
            }
            ivUserImage, tvMyProfile -> {
                mainActivity.addMainFragment(R.id.homeContainer, ProfileFragment())
            }
            tvSecure -> {
                mainActivity.addMainFragment(R.id.homeContainer, SecurityFragment())
            }
            tvShareApp -> {
                if (groupShare.visibility == View.GONE) {
                    tvShareApp.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_share,
                        0,
                        R.drawable.ic_up_arrow,
                        0
                    )
                    ViewAnimationUtils.expand(groupShare)
                } else {
                    tvShareApp.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_share,
                        0,
                        R.drawable.ic_down_arrow,
                        0
                    )
                    groupShare.onOffVisibility(false)
                }
                tvAboutMe.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_my_profile_on,
                    0,
                    R.drawable.ic_down_arrow,
                    0
                )
                groutAboutMe.onOffVisibility(false)
            }
            tvShareViaMsg -> shareWithSmsMsg("Join ${getString(R.string.app_name)}")
            tvShareViaInsta -> mAppUtils.shareApp(
                requireActivity(),
                "Join ${getString(R.string.app_name)}"
            )
            tvShareViaFacebook -> shareWithFb("Join ${getString(R.string.app_name)}")
            tvShareFeedback -> {
                mainActivity.addMainFragment(R.id.homeContainer, FeedbackFragment())
            }
            tvGetHelp -> {
                mainActivity.addMainFragment(R.id.homeContainer, HelpFragment())
            }
            tvHelpTicket -> {
                mainActivity.addMainFragment(R.id.homeContainer, HelpTicketFragment())
            }
            tvLogout -> {
                AllinOneDialog(ttle = "Sign out",
                    msg = getString(R.string.logout_message),
                    btnLeft = "No",
                    btnRight = "Yes",
                    onLeftClick = {},
                    onRightClick = {
                        onLogout("Successfully logged out")
                    })
            }
        }
    }

    fun shareWithFb(msg: String = "") {
        var facebookAppFound = false
        var shareIntent = Intent(android.content.Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
//        shareIntent.putExtra(Intent.EXTRA_TEXT, msg)
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(msg))

        val pm = activity!!.getPackageManager()
        val activityList = pm.queryIntentActivities(shareIntent, 0)
        for (app in activityList) {
            if (app.activityInfo.packageName.contains("com.facebook.katana")) {
                val activityInfo = app.activityInfo
                val name =
                    ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name)
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                shareIntent.component = name
                facebookAppFound = true
                break
            }
        }
        if (!facebookAppFound) {
            val sharerUrl = msg
            shareIntent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
            startActivity(shareIntent)
        } else mAppUtils.shareApp(requireActivity(), msg)
    }//end shareWithFb

    fun shareWithSmsMsg(msg: String = "") {
        try {
            val sendIntent = Intent(Intent.ACTION_VIEW)
            sendIntent.putExtra("sms_body", msg)
            sendIntent.type = "vnd.android-dir/mms-sms"
            startActivity(sendIntent)
        } catch (e: Exception) {
            val uri = Uri.parse(String.format("smsto:%s", ""))
            val sendIntent = Intent(Intent.ACTION_SENDTO, uri)
            sendIntent.putExtra("sms_body", msg)
            sendIntent.setPackage("com.google.android.apps.messaging")
            startActivity(sendIntent)
        }
    }//end shareWithSMS

    val mInfoBuilder: Dialog by lazy { Dialog(requireActivity()) }

    fun showLanguagePop() {
        mInfoBuilder.setContentView(R.layout.language_selection_popup);
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        mInfoBuilder.window!!.setBackgroundDrawableResource(R.drawable.shadow_border_bg_more_rounded)
        mInfoBuilder.window!!.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
            (displayMetrics.widthPixels * 0.99).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        if (mPrefs.getKeyValue(PreferenceConstants.LANGUAGE).equals("en")){
            mInfoBuilder.findViewById<CheckedTextView>(R.id.mTvEnglish).isChecked=true
        }else{
            mInfoBuilder.findViewById<CheckedTextView>(R.id.mTvSpanish).isChecked=true
        }


        mInfoBuilder.findViewById<CheckedTextView>(R.id.mTvEnglish)
            .setOnClickListener {
                mInfoBuilder.findViewById<CheckedTextView>(R.id.mTvEnglish).isChecked=true
                mInfoBuilder.findViewById<CheckedTextView>(R.id.mTvSpanish).isChecked=false
            }

        mInfoBuilder.findViewById<CheckedTextView>(R.id.mTvSpanish)
            .setOnClickListener {

                mInfoBuilder.findViewById<CheckedTextView>(R.id.mTvEnglish).isChecked=false
                mInfoBuilder.findViewById<CheckedTextView>(R.id.mTvSpanish).isChecked=true
            }

        mInfoBuilder.findViewById<TextView>(R.id.tvDone)
            .setOnClickListener {
                if (mInfoBuilder.findViewById<CheckedTextView>(R.id.mTvEnglish).isChecked==true){
                    mPrefs.setKeyValue(PreferenceConstants.LANGUAGE, "en")
                }else{
                    mPrefs.setKeyValue(PreferenceConstants.LANGUAGE, "es")
                }

                if (mPrefs.getKeyValue(PreferenceConstants.LANGUAGE).equals("en")){
                    tvLanguage.text="English"
                }else{
                    tvLanguage.text="Spanish"
                }
                mInfoBuilder.dismiss()
            }

        mInfoBuilder.findViewById<TextView>(R.id.tvCancel)
            .setOnClickListener {
                mInfoBuilder.dismiss()
            }
        mInfoBuilder.show();
    }
}