package com.app.givebackrx.appcode.main.charitydetail

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.givebackrx.R
import com.app.givebackrx.appcode.charityDetail.CharityDetailPresenter
import com.app.givebackrx.appcode.charityDetail.ICharityDetailView
import com.app.givebackrx.appcode.loginEmailPaswordModule.LoginEmailPasswordActivity
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.settings.mycharity.MyCharityFragment
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.Auth
import com.app.givebackrx.data.entity.CharityDetailData
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.*
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_charity_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_charity.tvTitle
import java.util.*
import javax.inject.Inject

class CharityDetailFragment : BaseFragment(), ICharityDetailView {

    val charity_id_extra by lazy { requireArguments().getString("charity_id_extra") ?: "" }
    val charity_name_extra by lazy { requireArguments().getString("charity_name_extra") ?: "" }
    var data:CharityDetailData?=null
    var alertDialog: AlertDialog?=null
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }

    var updated = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.activity_charity_detail, container, false)


    @Inject
    lateinit var presenter: CharityDetailPresenter<ICharityDetailView>


    override fun setUp(view: View) {
        presenter.onAttach(this)
        updated = false
        ivBack.setOnClickListener { requireActivity().onBackPressed() }

        clFrontView.setOnClickListener {
            if (data!=null)
                cardPopup(mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH).equals("gold"),true,data!!.card_detail.pcn,data!!.charity_detail.charity_logo?:"",data!!.card_detail.member_id?:"",data!!.card_detail.member_name?:"",data!!.card_detail.charity_name,data!!.card_detail.bin,data!!.card_detail.group,true)
        }
        clBackView.setOnClickListener {
            if (data!=null)
                cardPopup(mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH).equals("gold"),true,data!!.card_detail.pcn,data!!.charity_detail.charity_logo?:"",data!!.card_detail.member_id?:"",data!!.card_detail.member_name?:"",data!!.card_detail.charity_name,data!!.card_detail.bin,data!!.card_detail.group,false)
        }
        if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).contains("charity")) {
            tvSelected.onOffVisibility(tvSelectCharity.visibility == View.GONE)
            tvSelectCharity.onOffVisibility(tvSelectCharity.visibility == View.GONE)
        }

//        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
//            textView30.onOffVisibility(true)
//            mTvYourDonation.onOffVisibility(true)
//        }
        tvTitle.text = ""
        ivBack.setOnClickListener { requireActivity().onBackPressed() }
        mTvCharityName.text = charity_name_extra
        tvSkip.visibility = View.GONE
        mTvCardMessage.text =
            Html.fromHtml("This card is not insurance. This card may provide a discount on prescriptions at the pharmacy. Pharmacists with questions should call <font><b>(800) 650-1817</b></font>" + ". Customers please call " + "<font><b>(855) 769-6337</b></font>" + " or submit a request at " + "<font><b>www.givebackrx.com</b></font>")
        mTvPageMessage.text =
            Html.fromHtml("<font><b>Save money and giveback to " + "spread the love." + " Simply print your card, or receive it through email or text. Show it to your pharmacist to save money on prescriptions. Each time you save, give<b>back</b>Rx will donate to your charity.</b></font>")

        ivFlip.setOnClickListener {
            clFrontView.onOffCardVisibility(clBackView.visibility == View.VISIBLE)
            clBackView.onOffCardVisibility(clBackView.visibility == View.INVISIBLE)
        }
        if (isInternetConnected())
        {

            presenter.charityDetail(charity_id_extra)
        }

        mTvEmail.setOnClickListener {
            alertDialog=sendEmail("Enter your email address to send this card to:") {
                if (isInternetConnected()) {
                    hideKeyboardDialog(alertDialog!!.window!!.decorView)
                    with(JsonObject()) {
                        addProperty("email", it)
                        addProperty("charity_id", charity_id_extra)
                        addProperty("card_type", "charity")
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
            alertDialog= sendEmail("Enter your phone number to send this card to:",titl = "Phone number") {
                if (isInternetConnected()) {
                    hideKeyboardDialog(alertDialog!!.window!!.decorView)
                    with(JsonObject()) {
                        addProperty("phone", it.replace(Regex("[()-]"), "").replace(" ", ""))
                        addProperty("charity_id", charity_id_extra)
                        addProperty("card_type", "charity")
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

        tvSelectCharity.setOnClickListener {
            val login = mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)
            AllinOneDialog(ttle = if (login) "Update Charity" else "Select Charity",
                msg = if (login) "Would you like to select this charity?" else "Please login to select this charity for donations.",
                btnLeft = "Cancel",
                btnRight = if (login) "Update" else "Sign In",
                onLeftClick = {/*btn No click*/ },
                onRightClick = {/*btn Yes click*/
                    if (login) {
                        if (isInternetConnected()) presenter.selectCharity(charity_id_extra)
                    } else
                        requireActivity().startActivity(
                            Intent(
                                requireActivity(),
                                LoginEmailPasswordActivity::class.java
                            )
                        )
                }
            )
        }

        mTvUrl.setOnClickListener {
            if(mTvUrl.text.toString().trim().isNotEmpty()) {
                MovetoGoogle(requireActivity(),mTvUrl.text.toString().trim())
            }
        }

    }


    fun MovetoGoogle(activity: Activity, url: String) {
        var uri = Uri.parse(url)
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            uri = Uri.parse("http://$url")
        }
        try {
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            activity.startActivity(mapIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            openUrl(activity, uri)
        }
    }

    fun openUrl(activit: Activity, uri: Uri?) {
        try {
            val i = Intent("android.intent.action.MAIN")
            i.component =
                ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main")
            i.addCategory("android.intent.category.LAUNCHER")
            i.data = uri
            activit.startActivity(i)
        } catch (e: ActivityNotFoundException) {
            // Chrome is not installed
            try {
                val shareIntent = Intent(Intent.ACTION_VIEW)
                val pm = activit.packageManager
                val activityList =
                    pm.queryIntentActivities(shareIntent, 0)
                for (app in activityList) {
                    if (app.activityInfo.name.contains("chrome")) {
                        val activity = app.activityInfo
                        val name =
                            ComponentName(activity.applicationInfo.packageName, activity.name)
                        shareIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                        shareIntent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                        shareIntent.component = name
                        activit.startActivity(shareIntent)
                    }
                }
                activit.startActivity(
                    Intent.createChooser(
                        shareIntent,
                        activit.getString(R.string.app_name)
                    )
                )
            } catch (e1: ActivityNotFoundException) {
            }
        }
    }


    override fun onSignupResponse(it: SignInWithUserDetailEntity) {

    }

    override fun onCharityDetailResponse(
        data: CharityDetailData,
        auth: Auth
    ) {

        if (alertDialog!=null){
            if (alertDialog!!.isShowing)   alertDialog!!.dismiss()
        }
        this.data=data
        parentLayout.visibility=View.VISIBLE
        mTvYourDonation.text = data.charity_detail.donation_by_you
        mTvCharityName.text = data.charity_detail.charity_name
        mTvUrl.text = data.charity_detail.website
        mTvGiveBackDonation.text = data.charity_detail.donation_from_givebackrx
        val re = Regex("[^A-Za-z0-9 ]")
        mTvCategory.text = re.replace(data.charity_detail.category, "\n")
        mTvHeadquarters.text = "${data.charity_detail.charity_city}, ${data.charity_detail.charity_state}, USA"
        textView38.visibility=if ((data.card_detail.member_id?:"").isEmpty().not()) View.VISIBLE else View.INVISIBLE
        mTvMemberId.visibility=if ((data.card_detail.member_id?:"").isEmpty().not()) View.VISIBLE else View.INVISIBLE
        mTvMemberId.text = data.card_detail.member_id
        mTvBin.text = data.card_detail.bin
        mTvGroup.text = data.card_detail.group
        mTvPcn.text = data.card_detail.pcn
        mTvDesc.text = data.charity_detail.description
        mTvCardCharityName.text = "${data.card_detail.charity_name}"
        mIvImage.loadOrigImage(data.charity_detail.charity_logo)
        mIvCardCharityImageFront.loadOrigImage(data.charity_detail.charity_logo)

        tvSelected.onOffVisibility(data.charity_detail.selected)
        tvSelectCharity.onOffVisibility(!data.charity_detail.selected)
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(auth.new_jwt_token) { }
        } catch (e: Exception) {
        }
    }

    override fun onSelectedCharity(it: SuccessMessageEntity) {
        updated = true
        toast(it.message)
        if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).contains("charity").not()) {
            tvSelected.onOffVisibility(tvSelectCharity.visibility == View.VISIBLE)
            tvSelectCharity.onOffVisibility(tvSelectCharity.visibility == View.GONE)
        }
        Intent("UPDATE_CHARITY").apply {
            putExtra("updated", updated)
            LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(this)
        }
//        Intent("UPDATE_USER_CHARITY").apply {
//            putExtra("updated", true)
//            LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(this)
//        }
        mainActivity.addMainFragment(R.id.homeContainer, MyCharityFragment())
//        activity!!.onBackPressed()
    }


    override fun onCardViaEmailed(it: SuccessMessageEntity) {
        hideKeyboard()
        if (alertDialog!=null){
         if (alertDialog!!.isShowing)   alertDialog!!.dismiss()
        }
        toast(it.message, true)
        requireActivity().onBackPressed()
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

}