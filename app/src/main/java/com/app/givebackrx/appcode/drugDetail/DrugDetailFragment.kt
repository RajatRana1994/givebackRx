package com.app.givebackrx.appcode.drugDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.givebackrx.R
import com.app.givebackrx.appcode.loginEmailPaswordModule.LoginEmailPasswordActivity
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.main.charity.CharityFragment
import com.app.givebackrx.appcode.main.pharmacy.PharmacyFragment
import com.app.givebackrx.appcode.payment.PaymentFragment
import com.app.givebackrx.appcode.preSignUpModule.PreSignUpCheckActivity
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.CouponGenerationEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.*
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_drug_detail.*
import javax.inject.Inject

class DrugDetailFragment : BaseFragment(), IDrugDetailView {

    companion object {
        var pricebook_entry_id = ""
        var pharmacy_address_id = ""
    }
    var alertDialog: AlertDialog?=null

    var member_id = ""
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }
    @Inject
    lateinit var presenter: DrugDetailPresenter<IDrugDetailView>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_drug_detail, container, false)

    override fun setUp(view: View) {
        presenter.onAttach(this)
        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
            if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).equals("Silver")) {
                btnGBRxSilver.visibility = View.GONE

                if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).contains("charity").not()){
//                    view1.onOffVisibility(false)
//                    textView22.onOffVisibility(false)
//                    btnGBRxGold.onOffVisibility(false)
                    view1.onOffVisibility(true)
                    textView22.onOffVisibility(true)
                    btnGBRxGold.onOffVisibility(true)
                }else{
                    view1.onOffVisibility(false)
                    textView22.onOffVisibility(false)
                }
            } else if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).equals("Gold")){
                view1.visibility = View.GONE
                textView22.visibility = View.GONE
                btnGBRxSilver.visibility = View.GONE
                btnGBRxGold.visibility = View.GONE
            }
        }else{
            btnGBRxGold.visibility = View.GONE
        }
        ivBack.setOnClickListener { requireActivity().onBackPressed() }
        if (isInternetConnected())
            hashMapOf<String, String>().apply {
                put("pricebook_entry_id", pricebook_entry_id)
                put("pharmacy_address_id", pharmacy_address_id)
                if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
                    put("membership_type", mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE))
                presenter.couponGenerate(this)
            }

        mTvEmail.setOnClickListener {
            alertDialog=sendEmail("Enter your email address to send this card to:") {
                if (isInternetConnected()) {
                    hideKeyboardDialog(alertDialog!!.window!!.decorView)
                    with(JsonObject()) {
                        addProperty("email", it)
                        addProperty("member_id", member_id)
                        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
                            addProperty(
                                "membership_type",
                                mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
                            )
                        presenter.couponViaEmail(this)
                    }
                }
            }
        }
        mTvPhone.setOnClickListener {
            alertDialog= sendEmail(getString(R.string.send_coupon_via_phone),titl="Phone number") {
                if (isInternetConnected()) {
                    hideKeyboardDialog(alertDialog!!.window!!.decorView)
                    with(JsonObject()) {
                        addProperty("phone", it.replace(Regex("[()-]"), "").replace(" ", ""))
                        addProperty("member_id", member_id)
                        addProperty("pricebook_entry_id", pricebook_entry_id)
                        addProperty("pharmacy_address_id", pharmacy_address_id)
                        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
                            addProperty(
                                "membership_type",
                                mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
                            )
                        presenter.couponViaPhone(this)
                    }
                }
            }
        }
        ivFav.isChecked = PharmacyFragment.pharmacyData!!.isFavorite == "true"
        ivFav.setOnClickListener {
            if (isInternetConnected())
                if (ivFav.isChecked.not())
                    JsonObject().apply {
                        addProperty("user_type", mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                        addProperty("drug_name", PharmacyFragment.pharmacyData!!.drug_name)
                        addProperty(
                            "generic_name",
                            PharmacyFragment.pharmacyData!!.drug_generic_name
                        )
                        addProperty("form", PharmacyFragment.pharmacyData!!.form)
                        addProperty("dosage", PharmacyFragment.pharmacyData!!.dosage)
                        addProperty("quantity", PharmacyFragment.pharmacyData!!.quantity)
                        addProperty("drug_ndc", PharmacyFragment.pharmacyData!!.drugNdc)
                        addProperty(
                            "is_cust_qty_allowed",
                            PharmacyFragment.pharmacyData!!.isCustQtyAllowed
                        )
                        addProperty("cust_qty_type", PharmacyFragment.pharmacyData!!.custQtyType)
                        addProperty(
                            "internal_drug_pack_size",
                            PharmacyFragment.pharmacyData!!.internalDrugPackSize
                        )
                        addProperty(
                            "is_drug_generic",
                            PharmacyFragment.pharmacyData!!.isDrugGeneric
                        )
                        addProperty("is_location", PharmacyFragment.is_location)
                        if (PharmacyFragment.latitude.isNotEmpty())
                            addProperty("latitude", PharmacyFragment.latitude)
                        if (PharmacyFragment.longitude.isNotEmpty())
                            addProperty("longitude", PharmacyFragment.longitude)
                        presenter.addFaveMed(this)
                    }
        }

        btnGBRxSilver.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    requireActivity(),
                    LoginEmailPasswordActivity::class.java
                )
            )
        }
        btnGBRxGold.setOnClickListener {
            upgradeCard {

                val args = Bundle()
                args.putString("from_dashboard", "no")
                val paymentFragment = PaymentFragment()
                paymentFragment.setArguments(args)
                mainActivity.addMainFragment(R.id.homeContainer, paymentFragment)
            }
        }

    }


    override fun onAddedFavResp(it: SuccessMessageEntity) {
        ivFav.isChecked=true
        Intent("UPDATE_FAVS").apply {
            putExtra("updated", true)
            LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(this)
        }
    }

    fun updateAccordingPref() {
        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
            if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).equals("Silver")) {
                btnGBRxSilver.visibility = View.GONE
            } else {
                view1.visibility = View.GONE
                textView22.visibility = View.GONE
                btnGBRxSilver.visibility = View.GONE
                btnGBRxGold.visibility = View.GONE
            }
        }
    }

    override fun onCouponGenerated(it: CouponGenerationEntity) {
        clMid.visibility = View.VISIBLE
        member_id = it.data.member_id
        tvDrugName.text = it.data.pharmacy_name
        tvPharmacyName.text = it.data.drug_name
        tvPharmacyCate.text = it.data.drug_generic_final_name
        if (it.data.member_id.isNullOrEmpty()) {
            memberId.onOffVisibility(false)
            tvMemId.onOffVisibility(false)
        }
        ivPharmacyLogo.loadImageRadius(it.data.image_url)
        memberId.text = it.data.display_member_id
        binValue.text = it.data.rxbin
        pcnValue.text = it.data.rxpcn
        groupValue.text = it.data.rxgroup
        tvGeneric.text = it.data.drug_name
        tvDose.text = it.data.dosage
        tvQntys.text = it.data.quantity
        tvForm.text = it.data.form
        tvAboutCoupon.text =
            Html.fromHtml("<font> This coupon is not insurance. The amount in the coupon is the estimated discount provided at the register by the pharmacy. Pharmacists with questions should call <b> (800) 650-1817 </b>. Customers please call <b> (855) 769-6337 </b> or submit a request at <b> www.givebackrx.com. </b> </font>")
        tvPrice.text = "$${it.data.price}"
        tvPrice.setCompoundDrawablesWithIntrinsicBounds(
            if (it.data.is_lowest.equals("true")) R.drawable.ic_feedback else 0,
            0,
            0,
            0
        )
        if (it.data.distance.isNotEmpty()) {
            mTvDistance.visibility = View.VISIBLE
            textView54.visibility = View.VISIBLE
            mTvDistance.text = """${it.data.distance} miles"""
        }
        var address = it.data.pharmacy_street_address
        address += if (address.isEmpty()) it.data.pharmacy_city else ", ${it.data.pharmacy_city}"
        address += if (address.isEmpty()) it.data.pharmacy_state else ", ${it.data.pharmacy_state}"
        address += if (address.isEmpty()) it.data.pharmacy_zip_code else ", ${it.data.pharmacy_zip_code}"
        mTvAddress.visibility = View.VISIBLE
        textView55.visibility = View.VISIBLE
        mTvAddress.text = "$address"
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token) {
                    updateAccordingPref()
                }
        } catch (e: Exception) {
        }
    }


    override fun onCouponViaEmailed(it: SuccessMessageEntity) {
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