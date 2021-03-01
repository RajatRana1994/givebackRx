package com.app.givebackrx.appcode.orderSummary.addPaymentModule

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.givebackrx.R
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.payment.CardValidator
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.DefaultAddress
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.addWatcher
import com.app.givebackrx.utils.extension.onOffVisibility
import com.app.givebackrx.utils.extension.toast
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_add_new_card.*
import java.util.*
import javax.inject.Inject


class AddNewPaymentFragment : BaseFragment(), IAddNewPaymentView {

    var screen_type=""


    @Inject
    lateinit var presenter: AddNewPaymentPresenter<IAddNewPaymentView>
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_new_card, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)

        val frag_value = arguments
        if (frag_value!!.containsKey("screentype")) {
            screen_type= frag_value.getString("screentype").toString()
        }

        etCardNumber.addWatcher {
            cardTypeName.text = if (CardValidator.validateCardNumber(etCardNumber.text.toString()))
                (CardValidator.getCardType(etCardNumber.text.toString()).name) else ""
            cardTypeName.onOffVisibility(cardTypeName.text.isNotEmpty())
        }
        applyFilters()


        ivBack.setOnClickListener { requireActivity().onBackPressed() }
        btnNext.setOnClickListener { validate() }



    }

    override fun onAddedCardResp(it: SuccessMessageEntity) {
        toast(it.message, true)
        LocalBroadcastManager.getInstance(requireActivity())
            .sendBroadcast(Intent("Update_addresses").apply { putExtra("Added", "true") })
        Handler().postDelayed({ requireActivity().onBackPressed() }, 500)
    }



    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

    companion object {
        var mDefaultAddress: DefaultAddress? = null
    }


    private fun applyFilters() {
        etCardNumber.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            if (source.length > 0) {
                if (!Character.isDigit(source[0])) return@InputFilter "" else {
                    if (dstart == 4) {
                        return@InputFilter " $source"
                    } else if (dstart == 9) {
                        return@InputFilter " $source"
                    } else if (dstart == 14) return@InputFilter " $source"
                    else if (dstart > 18) return@InputFilter ""
                    else return@InputFilter "$source"
                }
            }
            null
        })
        etExpiry.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            if (source.length > 0) {
                if (!Character.isDigit(source[0])) {
                    return@InputFilter ""
                } else if (dstart == 1)
                    return@InputFilter "$source/"
                else if (dstart > 6)
                    return@InputFilter ""
                else return@InputFilter "$source"
            }
            null
        })
    }


    fun validate() {
        if (etFirstname.text.isEmpty()) toast("Enter First Name", false)
        else if (etLastname.text.isEmpty()) toast("Enter Last Name", false)
        else if (!CardValidator.validateCardNumber(etCardNumber.text.toString())) toast(
                "Invalid Card Number",
                false
        )
        else if (etCVV.text.isEmpty() && etCVV.text.toString().length < 3) toast(
                "Enter correct CVV",
                false
        )
        else {
            if (etExpiry.text.contains("/")) {
                if (CardValidator.validateExpiryDate(
                                etExpiry.text.toString().substringBefore("/"),
                                etExpiry.text.toString().substringAfter("/")
                        )
                ) {
                    JsonObject().apply {
                        addProperty("first_name", etFirstname.text.toString())
                        addProperty("last_name", etLastname.text.toString())
                        addProperty("card_number", CardValidator.sanitizeEntry(etCardNumber.text.toString(), true))
                        addProperty("expire_month", etExpiry.text.toString().substringBefore("/").replace("/", ""))
                        addProperty("expire_year", etExpiry.text.toString().substringAfter("/").replace("/", ""))
                        addProperty("cvv", etCVV.text.toString())
                        addProperty("card_type", CardValidator.getCardType(etCardNumber.text.toString()).name)
                        presenter.addNewAddress(this)
                    }
                } else toast("Invalid card Expiry", false)
            } else toast("Invalid card Expiry", false)
        }
    }

}