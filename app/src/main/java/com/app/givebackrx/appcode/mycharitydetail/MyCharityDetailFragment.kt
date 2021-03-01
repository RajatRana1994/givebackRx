package com.app.givebackrx.appcode.mycharitydetail

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.app.givebackrx.R
import com.app.givebackrx.appcode.editmycharity.EditMyCharityFragment
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.MyCharityDetailData
import com.app.givebackrx.data.entity.MyCharityDetailEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.*
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_my_charity_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class MyCharityDetailFragment : BaseFragment(), IMyCharityDetailView {

    var charity_id = ""
    var alertDialog: AlertDialog?=null

    @Inject
    lateinit var presenter: MyCharityDetailPresenter<IMyCharityDetailView>
    var data: MyCharityDetailData? = null
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_my_charity_detail, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        if (isInternetConnected())
            presenter.registeredCharityDetail()
        tvTitle.text=" My Charity"
        mTvCardMessage.text =
            Html.fromHtml("This card is not insurance. This card may provide a discount on prescriptions at the pharmacy. Pharmacists with questions should call <font><b>(800) 650-1817</b></font>" + ". Customers please call " + "<font><b>(855) 769-6337</b></font>" + " or submit a request at " + "<font><b>www.givebackrx.com</b></font>")
        mTvPageMessage.text =
            Html.fromHtml("<font><b>Save money and give<b>back</b> to " + "spread the love." + " Simply print your card, or receive it through email or text. Show it to your pharmacist to save money on prescriptions. Each time you save, give<b>back</b>Rx will donate to your charity.</b></font>")

        tvEdit.setOnClickListener {
            mainActivity.addMainFragment(
                R.id.homeContainer,
                EditMyCharityFragment()
            )
        }
        ivFlip.setOnClickListener {
            clFrontView.onOffCardVisibility(clBackView.visibility == View.VISIBLE)
            clBackView.onOffCardVisibility(clBackView.visibility == View.INVISIBLE)
        }

        clFrontView.setOnClickListener {
            if (data != null)
                cardPopup(mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH).equals("gold"),
                    true,
                    data!!.card_detail.pcn,
                    data!!.charity_detail.charity_logo ?: "",
                    data!!.card_detail.member_id ?: "",data!!.card_detail.member_name?:"",
                    data!!.card_detail.charity_name,
                    data!!.card_detail.bin,
                    data!!.card_detail.group,
                    true
                )
        }
        clBackView.setOnClickListener {
            if (data != null)
                cardPopup(mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH).equals("gold"),
                    true,
                    data!!.card_detail.pcn,
                    data!!.charity_detail.charity_logo ?: "",
                    data!!.card_detail.member_id ?: "",data!!.card_detail.member_name?:"",
                    data!!.card_detail.charity_name,
                    data!!.card_detail.bin,
                    data!!.card_detail.group,
                    false
                )
        }
        mTvEmail.setOnClickListener {
            if (charity_id.isNotEmpty())
               alertDialog= sendEmail("Enter your email address to send this card to:") {
                    if (isInternetConnected()) {
                        hideKeyboardDialog(alertDialog!!.window!!.decorView)
                        with(JsonObject()) {
                            addProperty("email", it)
                            addProperty("charity_id", charity_id)
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
            if (charity_id.isNotEmpty())
                alertDialog=sendEmail("Enter your phone number to send this card to:",titl = "Phone number") {
                if (isInternetConnected()) {
                    hideKeyboardDialog(alertDialog!!.window!!.decorView)
                    with(JsonObject()) {
                        addProperty("phone", it.replace(Regex("[()-]"), "").replace(" ", ""))
                        addProperty("charity_id", charity_id)
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

    }

    override fun onMyCharityDetailResponse(it: MyCharityDetailEntity) {
        this.data = it.data
        charity_id = it.data.charity_detail.charity_id
        parentLayout.visibility = View.VISIBLE
        mTvCharityName.text = it.data.charity_detail.charity_name
        mTvUrl.text = it.data.charity_detail.website
        mTvCategory.text = it.data.charity_detail.category
        mCharityAddress.text = it.data.charity_detail.charity_address
        mTvPrimaryPhone.text = it.data.charity_detail.signee_phone
        mTvFederalID.text = it.data.charity_detail.federal_id_number
        mTvStatus501.text = it.data.charity_detail.status_501
        mTvTotalDonations.text = it.data.charity_detail.donation_from_givebackrx
        mTvMailingAddress.text = it.data.charity_detail.charity_address_cheque
        mTvPreferredPayment.text = it.data.charity_detail.preferred_payment_method
        mTvCharityStatus.text = "Approval Status : ${it.data.charity_detail.status}"
        textView38.visibility =
            if ((it.data.card_detail.member_id ?: "").isEmpty().not()) View.VISIBLE else View.INVISIBLE
        mTvMemberId.visibility =
            if ((it.data.card_detail.member_id ?: "").isEmpty().not()) View.VISIBLE else View.INVISIBLE
        mTvMemberId.text = it.data.card_detail.member_id
        mTvBin.text = it.data.card_detail.bin
        mTvGroup.text = it.data.card_detail.group
        mTvPcn.text = it.data.card_detail.pcn
        mTvDesc.text = it.data.charity_detail.description
        mTvCardCharityName.text = it.data.charity_detail.charity_name
        mIvImage.loadOrigImage(it.data.charity_detail.charity_logo)
        mIvCardCharityImageFront.loadOrigImage(it.data.charity_detail.charity_logo)
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token){

                }
        }catch (e: Exception){}
    }

    override fun onCardViaEmailed(it: SuccessMessageEntity) {

        if (alertDialog!=null){
            if (alertDialog!!.isShowing)   alertDialog!!.dismiss()
        }
        hideKeyboard()
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