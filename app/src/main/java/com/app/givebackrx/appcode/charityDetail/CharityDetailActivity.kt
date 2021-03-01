package com.app.givebackrx.appcode.charityDetail

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.app.givebackrx.R
import com.app.givebackrx.appcode.selectCharityModule.SelectCharityActivity
import com.app.givebackrx.appcode.verifyOtpModule.VerifyOTPActivity
import com.app.givebackrx.base.BaseActivity
import com.app.givebackrx.data.entity.Auth
import com.app.givebackrx.data.entity.CharityDetailData
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.*
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_charity_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*
import java.util.*
import javax.inject.Inject

class CharityDetailActivity : BaseActivity(), ICharityDetailView {

    val charity_id_extra by lazy { intent.getStringExtra("charity_id_extra") ?: "" }
    val charity_name_extra by lazy { intent.getStringExtra("charity_name_extra") ?: "" }
    var data:CharityDetailData?=null
    @Inject
    lateinit var presenter: CharityDetailPresenter<ICharityDetailView>
    var alertDialog: AlertDialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charity_detail)
        presenter.onAttach(this)
        ivBack.setOnClickListener { finish() }
        ivFlip.setOnClickListener {
            if (clFrontView.visibility == View.VISIBLE) {
                clBackView.visibility = View.VISIBLE
                clFrontView.visibility = View.INVISIBLE
            } else {
                clBackView.visibility = View.INVISIBLE
                clFrontView.visibility = View.VISIBLE
            }
        }
        tvTitle.text = ""
        mTvCharityName.text = charity_name_extra
        tvSkip.visibility = View.GONE
        mTvCardMessage.text =
            Html.fromHtml("This card is not insurance. This card may provide a discount on prescriptions at the pharmacy. Pharmacists with questions should call <font><b>(800) 650-1817</b></font>" + ". Customers please call " + "<font><b>(855) 769-6337</b></font>" + " or submit a request at " + "<font><b>www.givebackrx.com</b></font>")
        mTvPageMessage.text =
            Html.fromHtml("<font><b>Save money and giveback to " + "spread the love." + " Simply print your card, or receive it through email or text. Show it to your pharmacist to save money on prescriptions. Each time you save, givebackRx will donate to your charity.</b></font>")

        clFrontView.setOnClickListener {
            if (data!=null)
                cardPopup(mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH).equals("gold"),true,data!!.card_detail.pcn,data!!.charity_detail.charity_logo?:"",data!!.card_detail.member_id?:"",data!!.card_detail.member_name?:"",data!!.card_detail.charity_name,data!!.card_detail.bin,data!!.card_detail.group,true)
        }
        clBackView.setOnClickListener {
            if (data!=null)
                cardPopup(mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH).equals("gold"),true,data!!.card_detail.pcn,data!!.charity_detail.charity_logo?:"",data!!.card_detail.member_id?:"",data!!.card_detail.member_name?:"",data!!.card_detail.charity_name,data!!.card_detail.bin,data!!.card_detail.group,false)
        }

        if (isInternetConnected())
            presenter.charityDetail(charity_id_extra)


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
            AllinOneDialog(ttle = "${charity_name_extra}",
                msg = "You have selected this charity for donations would you like to proceed with it.",
                onLeftClick = {/*btn No click */ },
                onRightClick = {/*/btn Yes click*/
                    SelectCharityActivity.signup_extra!!.put("charity_id", charity_id_extra)
                    SelectCharityActivity.signup_extra!!.put("next_page", true)
                    if (isInternetConnected())
                        presenter.signup(SelectCharityActivity.signup_extra!!)
                }
            )
        }

    }


    override fun onCharityDetailResponse(
        data: CharityDetailData,
        auth: Auth
    ) {
        this.data=data
        parentLayout.visibility=View.VISIBLE
        mTvCharityName.text = data.charity_detail.charity_name
        mTvUrl.text = data.charity_detail.website
        mTvGiveBackDonation.text = data.charity_detail.donation_from_givebackrx
        mTvCategory.text = data.charity_detail.category.replace(";",",")
        mTvHeadquarters.text = "${data.charity_detail.charity_city}, ${data.charity_detail.charity_state}, USA"
        textView38.visibility=if ((data.card_detail.member_id?:"").isEmpty().not()) View.VISIBLE else View.INVISIBLE
        mTvMemberId.visibility=if ((data.card_detail.member_id?:"").isEmpty().not()) View.VISIBLE else View.INVISIBLE
        mTvMemberId.text = data.card_detail.member_id
        mTvBin.text = data.card_detail.bin
        mTvGroup.text = data.card_detail.group
        mTvPcn.text = data.card_detail.pcn
        mTvDesc.text = data.charity_detail.description
        mTvCardCharityName.text =data!!.card_detail.member_name?:""
        mIvImage.loadOrigImage(data.charity_detail.charity_logo)
        mIvCardCharityImageFront.loadOrigImage(data.charity_detail.charity_logo)
    }

    override fun onSelectedCharity(it: SuccessMessageEntity) {

    }

    override fun onCardViaEmailed(it: SuccessMessageEntity) {
        if (alertDialog!=null){
            if (alertDialog!!.isShowing)   alertDialog!!.dismiss()
        }
        toast(it.message, true)
    }

    override fun onSignupResponse(it: SignInWithUserDetailEntity) {
        Intent(this@CharityDetailActivity, VerifyOTPActivity::class.java).apply {
            putExtra("value_extra", if (SelectCharityActivity.signup_extra!!.get("type")!!.equals("phone")) SelectCharityActivity.signup_extra!!.get("phone") as String else SelectCharityActivity.signup_extra!!.get("email") as String)
            putExtra("type_extra", SelectCharityActivity.signup_extra!!.get("type") as String)
            putExtra("page_extra", "signUp")
            putExtra("otp_extra", it.data.otp.toString())
            startActivity(this)
        }
    }

    override fun onGeneratedToken(lastAction: String) {

    }
}