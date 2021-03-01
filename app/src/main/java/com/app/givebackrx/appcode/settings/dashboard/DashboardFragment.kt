package com.app.givebackrx.appcode.settings.dashboard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.givebackrx.R
import com.app.givebackrx.appcode.loginEmailPaswordModule.LoginEmailPasswordActivity
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.main.charity.CharityFragment
import com.app.givebackrx.appcode.main.charitydetail.CharityDetailFragment
import com.app.givebackrx.appcode.main.settings.myNoticesModule.MyNoticesFragment
import com.app.givebackrx.appcode.main.settings.myTasksModule.MyTaskFragment
import com.app.givebackrx.appcode.main.settings.referralsModule.ReferralsFragment
import com.app.givebackrx.appcode.payment.PaymentFragment
import com.app.givebackrx.appcode.preSignUpModule.PreSignUpCheckActivity
import com.app.givebackrx.appcode.profileModule.ProfileFragment
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.DashboardData
import com.app.givebackrx.data.entity.DashboardEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.*
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.util.*
import javax.inject.Inject


class DashboardFragment : BaseFragment(), IDashboardView {

    @Inject
    lateinit var presenter: DashboardPresenter<IDashboardView>
    val isPortalUser by lazy {
        mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).contains("charity").not()
    }
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }

    var alertDialog: AlertDialog? = null

    var data: DashboardData? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        if (isInternetConnected())
            presenter.dashboard()

        setVisibilityAndText()
        ivBack.setOnClickListener { requireActivity().onBackPressed() }

        tvMyTask.setOnClickListener {
            mainActivity.addMainFragment(
                R.id.homeContainer,
                MyTaskFragment()
            )
        }

        tvMyReferral.setOnClickListener {
            mainActivity.addMainFragment(
                R.id.homeContainer,
                ReferralsFragment()
            )
        }

        tvMyNotices.setOnClickListener {
            mainActivity.addMainFragment(
                R.id.homeContainer,
                MyNoticesFragment()
            )
        }

        btnSelectNewCharity.setOnClickListener {

            val args = Bundle()
            args.putString("from_dashboard", "yes")
            val charityFragment = CharityFragment()
            charityFragment.setArguments(args)


            mainActivity.addMainFragment(R.id.homeContainer, charityFragment)
        }
        tvLogin.onOffVisibility(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN).not())
        tvSignup.onOffVisibility(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN).not())
        tvSignup.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    PreSignUpCheckActivity::class.java
                )
            )
        }
        tvLogin.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    LoginEmailPasswordActivity::class.java
                )
            )
        }
        ivUserImage.onOffVisibility(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
        ivUserImage.setOnClickListener {
            mainActivity.addMainFragment(
                R.id.homeContainer,
                ProfileFragment()
            )
        }
        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
            ivUserImage.loadImageRadius(mAppUtils.getUserData(mPrefs)!!.profile_pic ?: "", 100)
        mTvEmail.setOnClickListener {
            alertDialog = sendEmail(
                getString(R.string.enter_email_to_send_card),titl = "Email",
                terms = getString(R.string.terms_silver, if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).isEmpty()) "silver" else mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH)
                )
            ) {
                if (isInternetConnected()) {
                    hideKeyboardDialog(alertDialog!!.window!!.decorView)
                    with(JsonObject()) {
                        addProperty("email", it)
                        addProperty("charity_id", "")
                        addProperty("card_type", "user")
                        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
                            addProperty(
                                "membership_type",
                                mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
                            )
                        presenter.emailCard(this)
                    }
                }
            }
        }
        mTvPhone.setOnClickListener {
            alertDialog = sendEmail(
                getString(R.string.enter_phone_no_to_send_card), titl = getString(R.string.phoneno),
                terms = getString(R.string.terms_silver,
                    if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
                            .isEmpty()
                    ) "silver" else mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
                        .toLowerCase(Locale.ENGLISH)
                )
            ) {
                if (isInternetConnected()) {
                    hideKeyboardDialog(alertDialog!!.window!!.decorView)
                    with(JsonObject()) {
                        addProperty("phone", it.replace(Regex("[()-]"), "").replace(" ", ""))
//                        addProperty("charity_id", "")
                        addProperty("card_type", "user")
                        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
                            addProperty(
                                "membership_type",
                                mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
                            )
                        presenter.textCard(this)
                    }
                }
            }
        }

        updateAccordingPref()
        btnUpgrade.setOnClickListener {
            upgradeCard {
                val args = Bundle()
                args.putString("from_dashboard", "no")
                val paymentFragment = PaymentFragment()
                paymentFragment.setArguments(args)
                mainActivity.addMainFragment(R.id.homeContainer, paymentFragment)
            }
        }
        mTvEmail.onOffVisibility(isPortalUser)
        mTvPhone.onOffVisibility(isPortalUser)
        clFrontView.setOnClickListener {
            if (data != null)
                cardPopup(
                    mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH)
                        .equals("gold"),
                    false,
                    data!!.card_detail.pcn,
                    data!!.charity!!.charity_logo ?: "",
                    data!!.card_detail.member_id ?: "",
                    data!!.card_detail.member_name ?: "",
                    data!!.charity!!.charity_name!!,
                    data!!.card_detail.bin,
                    data!!.card_detail.group,
                    true
                )
        }
        clBackView.setOnClickListener {
            if (data != null)
                cardPopup(
                    mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH)
                        .equals("gold"),
                    false,
                    data!!.card_detail.pcn,
                    data!!.charity!!.charity_logo ?: "",
                    data!!.card_detail.member_id ?: "",
                    data!!.card_detail.member_name ?: "",
                    data!!.charity!!.charity_name!!,
                    data!!.card_detail.bin,
                    data!!.card_detail.group,
                    false
                )
        }
        clFrontViewWhite.setOnClickListener {
            if (data != null)
                cardPopup(
                    mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH)
                        .equals("gold"),
                    true,
                    data!!.card_detail.pcn,
                    data!!.charity!!.charity_logo ?: "",
                    data!!.card_detail.member_id ?: "",
                    data!!.card_detail.member_name ?: "",
                    data!!.charity!!.charity_name!!,
                    data!!.card_detail.bin,
                    data!!.card_detail.group,
                    true
                )
        }
        clBackViewWhite.setOnClickListener {
            if (data != null)
                cardPopup(
                    mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH)
                        .equals("gold"),
                    true,
                    data!!.card_detail.pcn,
                    data!!.charity_logo ?: "",
                    data!!.card_detail.member_id ?: "",
                    data!!.card_detail.member_name ?: "",
                    data!!.charity!!.charity_name!!,
                    data!!.card_detail.bin,
                    data!!.card_detail.group,
                    false
                )
        }

        ivFlip.setOnClickListener {
            clFrontView.onOffCardVisibility(clBackView.visibility == View.VISIBLE)
            clBackView.onOffCardVisibility(clBackView.visibility == View.INVISIBLE)
        }
        ivFlipWhite.setOnClickListener {

            clFrontViewWhite.onOffCardVisibility(clBackViewWhite.visibility == View.VISIBLE)
            clBackViewWhite.onOffCardVisibility(clBackViewWhite.visibility == View.INVISIBLE)
        }
    }

    private fun updateAccordingPref() {
        if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH)
                .equals("gold")
        ) {
            clFrontView.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.gold_card_gradient_bg)
            clBackView.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.gold_card_gradient_bg)
            btnUpgrade.onOffVisibility(false)
        }
    }

    private fun setVisibilityAndText() {
        mTvEmail.onOffVisibility(isPortalUser.not())
        mTvPhone.onOffVisibility(isPortalUser.not())
        cardViewWhite.onOffVisibility(isPortalUser.not())
        ivFlipWhite.onOffVisibility(isPortalUser.not())
        cardView.onOffVisibility(isPortalUser)
        ivFlip.onOffVisibility(isPortalUser)
        btnUpgrade.onOffVisibility(isPortalUser)
        if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).contains("charity").not()){
//            btnUpgrade.onOffVisibility(false)
            btnUpgrade.onOffVisibility(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN) && mPrefs.getKeyValue(
                PreferenceConstants.MEMBER_TYPE
            ).toLowerCase(Locale.ENGLISH).equals("gold").not())
        }

        cardViewCharity.onOffVisibility(isPortalUser)

        cardViewVideo.onOffVisibility(isPortalUser.not())
        cardViewMarketing.onOffVisibility(isPortalUser.not())

        tvMyMarketingPacketMsg.text =
            Html.fromHtml("Use the following URL when publishing your participation in give<b>back</b>Rx. All users who click this link will be defaulted to your charity when they sign up.")
        tvAboutgivebackRxPowerPoint.text = Html.fromHtml("About give<b>back</b>Rx PowerPoint")
        tvAboutgivebackRxLetter.text = Html.fromHtml("About give<b>back</b>Rx Letter")

    }

    override fun onDashboardResp(charity: DashboardEntity) {
        parentview.onOffVisibility(true)
        data = charity.data
        tvMoneySaved.text = "$ "+charity.data.saving_amount
        tvPendingTask.text = (charity.data.active_task_count ?: 0).toString()
        tvReferralCount.text = (charity.data.referral_made_count ?: 0).toString()
        tvNoticeCount.text = (charity.data.notice_reviewed_count ?: 0).toString()
        //for user card

        mTvPcn.text = charity.data.card_detail.pcn
        mTvMemberId.text = charity.data.card_detail.member_id
        mTvCardCharityName.text = charity.data.charity!!.charity_name
        mTvBin.text = charity.data.card_detail.bin
        mTvMemberName.text = data!!.card_detail.member_name ?: ""
        mTvGroup.text = charity.data.card_detail.group
        //for charity card
        mTvPcnWhite.text = charity.data.card_detail.pcn
        mTvMemberIdWhite.text = charity.data.card_detail.member_id
        mTvCardCharityNameWhite.text = charity.data.charity!!.charity_name
        mTvBinWhite.text = charity.data.card_detail.bin
        mTvGroupWhite.text = charity.data.card_detail.group
        if (isPortalUser) {
            tvCharityName.text = charity.data.charity!!.charity_name
            imageView3.loadOrigImage(charity.data.charity.charity_logo ?: "")
            tvCharityDesc.text = charity.data.charity!!.description ?: ""
            tvViewMore.setOnClickListener {
                CharityDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString("charity_id_extra", charity.data.charity.charity_id)
                        putString("charity_name_extra", charity.data.charity.charity_name)
                    }
                    this@DashboardFragment.mainActivity.addMainFragment(R.id.homeContainer, this)
                }
            }
        } else {
            // for charity user
        }

        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(charity.auth.new_jwt_token) {
                    updateAccordingPref()
                }
        } catch (e: Exception) {
        }

    }

    override fun onCardViaEmailed(it: SuccessMessageEntity) {
        if (alertDialog != null) {
            if (alertDialog!!.isShowing) alertDialog!!.dismiss()
        }
        hideAllTypeKB(requireView().rootView.windowToken)
        toast(it.message, true)
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }


    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
            broardReceiver,
            IntentFilter("UPDATE_USER_CHARITY")
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(broardReceiver)
    }

    val broardReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {

            if (isInternetConnected())
                presenter.dashboard()
        }
    }

}