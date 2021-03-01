package com.app.givebackrx.appcode.selectaddress.addnewaddress

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
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.givebackrx.GBRxApp
import com.app.givebackrx.R
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.DefaultAddress
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.data.entity.SuccessModelResponse
import com.app.givebackrx.utils.AppConstants
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.toast
import com.google.android.libraries.places.api.model.AddressComponent
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_add_new_address.*
import kotlinx.android.synthetic.main.fragment_add_new_address.ivBack
import kotlinx.android.synthetic.main.fragment_add_new_address.tvTitle
import java.util.*
import javax.inject.Inject


class AddNewAddressFragment : BaseFragment(), IAddNewAddressView {
    val statesList by lazy { GBRxApp().addStates(requireActivity()) }
    var arrayAdapter: ArrayAdapter<String>? = null

    var screen_type=""


    @Inject
    lateinit var presenter: AddNewAddressPresenter<IAddNewAddressView>
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
        return inflater.inflate(R.layout.fragment_add_new_address, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)

        val frag_value = arguments
        if (frag_value!!.containsKey("screentype")) {
            screen_type= frag_value.getString("screentype").toString()
            if (frag_value.getString("screentype").equals("Shipping")){
                tvTitle.text="Add Shipping Address"
            }else if (frag_value.getString("screentype").equals("Billing")){
                tvTitle.text="Add Billing Address"

            }

        }

//        if (isInternetConnected())
        setDataForEdit()
        arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            statesList.map { it.state }.toList()
        )
        etPhone.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            if (source.length > 0) {
                if (!Character.isDigit(source[0])) return@InputFilter "" else {
                    if (dstart == 0) {
                        return@InputFilter "($source"
                    } else if (dstart == 3) {
                        return@InputFilter "$source) "
                    } else if (dstart == 9) return@InputFilter "-$source"
                    else if (dstart >= 14) return@InputFilter ""
                }
            }
            null
        })
        edtState.adapter = arrayAdapter
        ivBack.setOnClickListener { requireActivity().onBackPressed() }
        etAddressa.setOnClickListener { openPlaceDialog() }
        btnNext.setOnClickListener { addAddress() }


    }

    private fun setDataForEdit() {
        if (mDefaultAddress != null) {
            tvTitle.text="Edit Address"
            btnNext.text="Update"
            spinnerAddressType.setSelection(
                when(mDefaultAddress!!.address_type){
                    "Home"->1
                    "Work"->2
                    "Shipping Address"->3
                    "Billing Address"->4
                    else->0
                }
            )
            etAptSuite.setText(mDefaultAddress!!.apt_suit_number.toString())
            etAddress.setText(mDefaultAddress!!.address.toString())
            edtCity.setText(mDefaultAddress!!.city.toString())
            edtZIPCode.setText(mDefaultAddress!!.zip_code.toString())
            etPhone.setText(getString(R.string.phone_format,mDefaultAddress!!.phone.toString().substring(0,3),mDefaultAddress!!.phone.toString().substring(3,6),mDefaultAddress!!.phone.toString().takeLast(4)))
            statesList.forEachIndexed { index, stateModel ->
                if (stateModel.code== mDefaultAddress!!.state||stateModel.state== mDefaultAddress!!.state){
                    edtState.setSelection(index)
                }
            }
        }
    }

    private fun addAddress() {
//        if (spinnerAddressType.selectedItemPosition == 0) toast("Select address type")
        if (etFname.text.isEmpty()) toast("Enter first name")
        else if (etLname.text.isEmpty()) toast("Enter last name")
//        else if (etAddressa.text.isEmpty()) toast("Enter address")
        else if (etAddress.text.isEmpty()) toast("Enter address")
        else if (edtCity.text.isEmpty()) toast("Enter city")
//        if (edtState.text.isEmpty()) toast("Enter")
        else if (edtZIPCode.text.isEmpty()) toast("Enter Zip code")
        else if (etPhone.text.isEmpty()) toast("Enter phone number")
        else if (etPhone.text.length < 14) toast("Enter valid phone number")
        else {
            if (isInternetConnected())
                JsonObject().apply {
                    addProperty("user_type", mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                    addProperty("address_type",screen_type )
                    addProperty("first_name",etFname.text.toString().trim() )
                    addProperty("last_name",etLname.text.toString().trim() )
                    addProperty("apt_suite_number", etAptSuite.text.toString())
                    addProperty("address", etAddress.text.toString())
                    addProperty("city", edtCity.text.toString())
                    addProperty("country", "US")
                    addProperty("state", statesList.get(edtState.selectedItemPosition).code.trim())
                    addProperty("zip_code", edtZIPCode.text.toString())
                    addProperty("phone", etPhone.text.toString().trim().replace(Regex("[()-]"), "").replace(" ", ""))
                    if (mDefaultAddress!=null) {
                        addProperty("address_id", mDefaultAddress!!.address_id)
                        presenter.updateNewAddress(this)
                    }else presenter.addNewAddress(this)
                }
        }
    }

    override fun onAddedAddressResp(it: SuccessMessageEntity) {
        toast(it.message, true)
        LocalBroadcastManager.getInstance(requireActivity())
            .sendBroadcast(Intent("ADDRESS_CHANGED").apply {
                putExtra("Added", "true")
            })
        Handler().postDelayed({ requireActivity().onBackPressed() }, 500)
    }

    fun openPlaceDialog() {
        val fields = Arrays.asList(
            Place.Field.PHONE_NUMBER,
            Place.Field.BUSINESS_STATUS,
            Place.Field.ADDRESS,
            Place.Field.NAME,
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.ID
        )
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(requireActivity())
        startActivityForResult(intent, AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                try {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    setPlaceData(place)
                } catch (e: java.lang.Exception) {
                }
            }
        }
    }

    private fun setPlaceData(placeData: Place) {
        try {
            etAddress.setText("")
            etAddressa.setText("")
            edtCity.setText("")
//            etState.setText("")
            if (!placeData.getName()!!.toLowerCase(Locale.ENGLISH).isEmpty()) {
                etAddressa.setText(placeData.getName())
                etAddress.setText(placeData.getName())
            }

            for (i in 0 until placeData.getAddressComponents()!!.asList().size) {
                val place: AddressComponent = placeData.getAddressComponents()!!.asList().get(i)
                if (place.types.contains("locality"))
                    edtCity.setText(place.name)
//                if (place.types.contains("administrative_area_level_1"))
//                    etState.setText(place.name)
                if (place.types.contains("postal_code"))
                    edtZIPCode.setText(place.name)
                if (place.types.contains("street_number")) {
                    etAddressa.setText("${etAddressa.text} ${place.name}")
                    etAddress.setText("${etAddress.text} ${place.name}")
                }
//                if (place.types.contains("country")) {
//                    etCountry.setText("${place.name}")
//                }

            }
        } catch (e: java.lang.Exception) {
            Log.e("Exce......", e.toString())
        }

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
}