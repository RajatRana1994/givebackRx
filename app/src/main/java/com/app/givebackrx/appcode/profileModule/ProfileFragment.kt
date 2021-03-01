package com.app.givebackrx.appcode.profileModule


import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.app.givebackrx.GBRxApp
import com.app.givebackrx.R
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.*
import com.app.givebackrx.utils.*
import com.app.givebackrx.utils.extension.loadImageRadius
import com.app.givebackrx.utils.extension.onOffVisibility
import com.app.givebackrx.utils.extension.toast
import com.bruce.pickerview.popwindow.DatePickerPopWin
import com.chaos.view.PinView
import com.google.android.libraries.places.api.model.AddressComponent
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.shawnlin.numberpicker.NumberPicker
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.etAddressa
import kotlinx.android.synthetic.main.fragment_profile.etAptNumber
import kotlinx.android.synthetic.main.fragment_profile.etCity
import kotlinx.android.synthetic.main.fragment_profile.etCountry
import kotlinx.android.synthetic.main.fragment_profile.etDob
import kotlinx.android.synthetic.main.fragment_profile.etEmailId
import kotlinx.android.synthetic.main.fragment_profile.etFname
import kotlinx.android.synthetic.main.fragment_profile.etLname
import kotlinx.android.synthetic.main.fragment_profile.etPhonenNo
import kotlinx.android.synthetic.main.fragment_profile.etState
import kotlinx.android.synthetic.main.fragment_profile.etZipcode
import kotlinx.android.synthetic.main.fragment_profile.spinnerGender
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


class ProfileFragment : BaseFragment(), IProfileView, View.OnClickListener {
    companion object {
        var picture_url = ""
    }

    var alertDialog: AlertDialog? = null

    val calendar: Calendar by lazy { Calendar.getInstance(Locale.ENGLISH) }


    var profileData: ProfileData? = null


    @Inject
    lateinit var presenter: ProfilePresenter<IProfileView>
    val statesList by lazy { GBRxApp().addStates(requireActivity()) }
    var arrayAdapter: ArrayAdapter<String>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private fun addPhoneFormat() {
        val filter = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
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
        etSecPhonenNo.filters = filter
        etPhonenNo.filters = filter
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        calendar.add(Calendar.YEAR, -18)

        arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            statesList.map { it.state }.toList()
        )
        etState.adapter = arrayAdapter

        editableView(false)
        if (isInternetConnected()) presenter.profileDetail()
//        addPhoneFormat()
        ivBack.setOnClickListener(this)
        ivEditUserEmail.setOnClickListener(this)
        ivEditUserPhone.setOnClickListener(this)
        ivEditUserSecPhone.setOnClickListener(this)
        tvEdit.setOnClickListener(this)
        etDob.setOnClickListener(this)
        ivEditUser.setOnClickListener(this)
        etAddressa.setOnClickListener(this)
        ivEditUserSecondaryEmail.setOnClickListener(this)
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View) {
        when (p0) {
            ivBack -> {
                requireActivity().onBackPressed()
            }
            ivEditUserEmail -> {
                alertDialog = sendEmail(
                    "Add Primary Email",
                    "To add a primary email to your account, please enter it below. We will send a 6-digit code to this email for verification."
                ) {
                    if (isInternetConnected())
                        hideKeyboardDialog(alertDialog!!.window!!.decorView)
                        presenter.updateSecondary(value = it, type = "primary_email")
                }
            }
            tvEdit -> {
                if (tvEdit.text.equals("Save")) {
                    if (isInternetConnected()) {
                        if (etFname.text.toString().isEmpty()) toast("Enter first name", false)
                        else if (etLname.text.toString().isEmpty()) toast("Enter last name", false)
                        else if (etAddressa.text.toString().isEmpty()) toast("Enter Address", false)
                        else if (etCity.text.toString().isEmpty()) toast("Enter City", false)
                        else if (etDob.text.toString().isEmpty()) toast(
                            "Enter Date of birth",
                            false
                        )
//                        else if (etAptNumber.text.toString().isEmpty()) toast("Enter Apt Suite", false)
                        else if (etZipcode.text.toString().isEmpty()) toast("Enter Zip code", false)
                        else {
                            presenter.editProfile(
                                etFname.text.toString(),
                                etLname.text.toString(),
                                etAddressa.text.toString(),
                                etCity.text.toString(),
                                statesList.get(etState.selectedItemPosition).code.toString(),
                                etCountry.text.toString(),
                                etDob.text.toString(),
                                spinnerGender.selectedItem.toString(),
                                etAptNumber.text.toString(),
                                etZipcode.text.toString()
                            )
                        }
                    }
                    editableView(false)
                } else {
                    editableView(true)
                }
            }
            ivEditUser -> {
                if (permissionFile.checkLocStorgePermission(requireActivity())) {
                    showImagePop()
                }
            }
            etDob -> if (tvEdit.text == "Save") {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    dobPopup()
                }
//                datePicker()
//                DatePickerDialog(
//                    requireActivity(),
//                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
//                        etDob.text = /*mAppUtils.convertDateFormat(*/
//                            "${if ((month + 1) > 9) month + 1 else "0${month + 1}"}-${if (dayOfMonth > 9) dayOfMonth else "0$dayOfMonth"}-$year"
////                            "yyyy-MM-dd",
////                            "dd/MM/yyyy"
////                        )
//                    },
//                    calendar.get(Calendar.YEAR),
//                    calendar.get(Calendar.MONTH),
//                    calendar.get(Calendar.DAY_OF_MONTH)
//                ).show()
            }
            etAddressa -> if (tvEdit.text == "Save") openPlaceDialog()
            ivEditUserSecondaryEmail -> {
                alertDialog = sendEmail(
                    "Add Secondary Email",
                    "To add a secondary email to your account, please enter it below. We will send a 6-digit code to this email for verification."
                ) {
                    if (isInternetConnected())
                        hideKeyboardDialog(alertDialog!!.window!!.decorView)
                        presenter.updateSecondary(value = it, type = "secondary_email")
                }
            }
            ivEditUserSecPhone -> {
                alertDialog = sendPhone(
                    "Add Secondary Phone Number",
                    "To add a secondary phone number to your account, please enter it below. We will send a 6-digit code to this phone number for verification."
                ) {
                    if (isInternetConnected())
                        presenter.updateSecondary(value = it, type = "secondary_cell_number")
                }
            }
            ivEditUserPhone -> {
                alertDialog = sendPhone(
                    "Update Primary Phone Number",
                    "To update a primary phone Number to your account, please enter it below. We will send a 6-digit code to this phone Number for verification."
                ) {
                    if (isInternetConnected())
                        presenter.updateSecondary(value = it, type = "primary_cell_number")
                }
            }
        }
    }

    fun datePicker() {
        val pickerPopWin: DatePickerPopWin =
            DatePickerPopWin.Builder(requireActivity(),
                DatePickerPopWin.OnDatePickedListener { year, month, day, dateDesc ->
                    etDob.text =
                        "${if ((month + 1) > 9) month + 1 else "0${month + 1}"}-${if (day > 9) day else "0$day"}-$year"
                }).textConfirm("CONFIRM") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(18) // button text size
                .viewTextSize(22) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#DA354E")) //color of confirm button
                .minYear(1900) //min year in loop
                .maxYear(Calendar.getInstance().get(Calendar.YEAR) - 18) // max year in loop
                .showDayMonthYear(true) // shows like dd mm yyyy (default is false)
                .dateChose("2003-01-01") // date chose when init popwindow
                .build()
        pickerPopWin.showPopWin(requireActivity())
    }

// implementation 'com.github.florent37:singledateandtimepicker:2.1.3'
//      private fun dateTimePicker() {
//          val singleBuilder = SingleDateAndTimePickerDialog.Builder(activity!!)
//                  .bottomSheet()
//                  .curved()
//                  .defaultDate(AppUtil.convertDateToDate(viewModel.selectedDate.get()!!, "yyyy MMM dd   hh:mm   a", "EEE dd MMM"))
//                  .backgroundColor(ContextCompat.getColor(activity!!, R.color.light_gray))
//                  .mainColor(Color.BLACK)
//                  .title(getString(R.string.date_and_time))
//                  .displayMinutes(true)
//                  .displayHours(true)
//                  .displayAmPm(true)
//                  .displayDays(true)
//                  .listener { date ->
//                      viewModel.selectedDate.set(simpleDateFormat.format(date))
//                      viewModel.checkoutDateForServer.set(AppUtil.dateUTC(date))
//                  }
//          singleBuilder!!.display()
//      }

    override fun onEditProfileResponse(it: VerifySecondaryOtpEntity) {
        if (it.success) {
            toast(it.message)
            tvName.text = "${etFname.text} ${etLname.text}"
            profileData!!.name = tvName.text.toString()
            profileData!!.first_name = etFname.text.toString()
            profileData!!.last_name = etLname.text.toString()
            profileData!!.cell_number =
                etPhonenNo.text.toString().replace(Regex("[()-]"), "").replace(" ", "")
            profileData!!.secondary_cell_number =
                etSecPhonenNo.text.toString().replace(Regex("[()-]"), "").replace(" ", "")
            profileData!!.email = tvEmail.text.toString()
            profileData!!.secondary_email = etEmailId.text.toString()
            profileData!!.address = etAddressa.text.toString()
            profileData!!.apt_suite_number = etAptNumber.text.toString()
            profileData!!.city = etCity.text.toString()
//            profileData!!.state = etState.text.toString()
            profileData!!.postal_code = etZipcode.text.toString()
//            profileData!!.country = etCountry.text.toString()
            updateSession()
        } else {
            if (profileData != null) {
                tvName.setText(profileData!!.name)
                etFname.setText(profileData!!.first_name)
                etLname.setText(profileData!!.last_name)
                etPhonenNo.setText(insertString(profileData!!.cell_number!!))
                etSecPhonenNo.setText(insertString(profileData!!.secondary_cell_number!!))
                tvEmail.setText(profileData!!.email)
                etEmailId.setText(profileData!!.secondary_email)
                etAddressa.setText(profileData!!.address)
                etAptNumber.setText(profileData!!.apt_suite_number)
                etCity.setText(profileData!!.city)
//                etState.setText(profileData!!.state)
                etZipcode.setText(profileData!!.postal_code.toString())
//                etCountry.setText(profileData!!.country)
            }
        }
    }

    fun insertString(originalString: String): String? {
        /* // Create a new string
         var newString: String? = String()
         for (i in 0 until originalString.length) {
             // Insert the original string character
             // into the new string
             if (i==0) newString="("+ originalString[i]
             else if(i==2) newString += originalString[i]+")"
             else if(i==5) newString += originalString[i]+"-"
 //            newString += originalString[i]
 //            if (i == index) {
 //                 Insert the string to be inserted
 //                 into the new string
 //                newString += stringToBeInserted
             }
 //        }
         // return the modified String*/
        return originalString
    }

    override fun onProfileResponse(it: ProfileEntity) {
        parentLayout.onOffVisibility(true)
        profileData = it.data
        ivUser.loadImageRadius(it.data.profile_image ?: "")
        tvName.setText(it.data.name)
        etFname.setText(it.data.first_name)
        etLname.setText(it.data.last_name)
        etPhonenNo.setText(insertString(it.data.cell_number!!))
        etSecPhonenNo.setText(insertString(it.data.secondary_cell_number!!))
        tvEmail.setText(it.data.email)
        etEmailId.setText(it.data.secondary_email)
        etAddressa.setText(it.data.address)
        etAptNumber.setText(
            (if (it.data.apt_suite_number != null) it.data.apt_suite_number else "")!!.replace(
                "null",
                ""
            )
        )
        etCity.setText(it.data.city)
//        etState.setText(it.data.state)
        var pos = 0
        for (i in 0 until statesList.size) {
            if (statesList[i].code.equals(it.data.state)) pos = i
        }
        etState.setSelection(pos)
        etDob.setText(it.data.dob!!.replace("null", ""))
        etZipcode.setText(it.data.postal_code.toString().replace("null", ""))
//        etCountry.setText(it.data.country)
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token) {  }
        } catch (e: Exception) {
        }
    }


    fun sendEmail(
        Heading: String = "",
        msg: String = "",
        onSend: (email: String) -> Unit
    ): AlertDialog {
        val builder = AlertDialog.Builder(requireActivity())
        val viewDialog = this.layoutInflater.inflate(R.layout.profile_addemail_dialog, null)
        builder.setView(viewDialog)
        val alertDialog = builder.create()
        viewDialog.findViewById<TextView>(R.id.tvHeading).text = Heading
        viewDialog.findViewById<TextView>(R.id.tvAddEmailMsg).text = msg
        viewDialog.findViewById<ImageView>(R.id.ivClose)
            .setOnClickListener { alertDialog.dismiss() }
        val edtEmail = viewDialog.findViewById<EditText>(R.id.edtEmail)

        viewDialog.findViewById<TextView>(R.id.btnSend).setOnClickListener {
//            alertDialog.dismiss()
            if (edtEmail.text.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString())
                    .matches().not()
            ) toast("Enter Valid Email", false)
            else {
                hideKeyboardDialog(alertDialog.currentFocus)
                onSend(edtEmail.text.toString())
            }
        }

        alertDialog.setOnDismissListener {
            hideKeyboardDialog(alertDialog.currentFocus)
        }
        alertDialog.setCancelable(true)
        alertDialog.show()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window?.getAttributes())
        val dialogWindowWidth = (displayWidth * 0.9f).toInt()
        layoutParams.width = dialogWindowWidth
        alertDialog.window?.attributes = layoutParams
        return alertDialog
    }

    fun sendPhone(
        Heading: String = "",
        msg: String = "",
        onSend: (phone: String) -> Unit
    ): AlertDialog {
        val builder = AlertDialog.Builder(requireActivity())
        val viewDialog = this.layoutInflater.inflate(R.layout.profile_addemail_dialog, null)
        builder.setView(viewDialog)
        val alertDialog = builder.create()
        viewDialog.findViewById<TextView>(R.id.tvHeading).text = Heading
        viewDialog.findViewById<TextView>(R.id.tvAddEmailMsg).text = msg
        viewDialog.findViewById<TextView>(R.id.tvEmailAddress).text = "Phone number"
        viewDialog.findViewById<ImageView>(R.id.ivClose)
            .setOnClickListener { alertDialog.dismiss() }
        val edtEmail = viewDialog.findViewById<EditText>(R.id.edtEmail)
        val edtPhone = viewDialog.findViewById<EditText>(R.id.edtPhone)
        edtEmail.visibility = View.GONE
        edtPhone.visibility = View.VISIBLE

        edtPhone.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
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

        viewDialog.findViewById<TextView>(R.id.btnSend).setOnClickListener {
//            alertDialog.dismiss()
            if (edtPhone.text.isEmpty() || edtPhone.text.length < 14)
                toast("Enter Valid Phone Number", false)
            else {
                hideKeyboardDialog(alertDialog.currentFocus)
                onSend(edtPhone.text.toString().replace(Regex("[()-]"), "").replace(" ", ""))
            }
        }

        alertDialog.setOnDismissListener {
            hideKeyboardDialog(alertDialog.currentFocus)
        }
        alertDialog.setCancelable(true)
        alertDialog.show()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window?.getAttributes())
        val dialogWindowWidth = (displayWidth * 0.9f).toInt()
        layoutParams.width = dialogWindowWidth
        alertDialog.window?.attributes = layoutParams
        return alertDialog
    }

    override fun onUpdateSecondaryResp(
        it: VerifySecondaryOtpEntity,
        value: String,
        type: String
    ) {

        if (alertDialog != null) {
            if (alertDialog!!.isShowing) alertDialog!!.dismiss()
        }
        alertDialog =
            verifyOtp(email = value, otp = it.data.redisOTP.toString(), type = type) { otp, email ->
                if (isInternetConnected())
                    JsonObject().apply {
                        addProperty("user_type", mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                        addProperty("type", type)
                        addProperty("value", email)
                        addProperty("otp", otp)
                        presenter.verifySecondaryOTP(this, email, type)
                    }
            }
    }

    fun verifyOtp(
        email: String = "",
        otp: String = "",
        type: String = "",
        onVerify: (otp: String, email: String) -> Unit
    ): AlertDialog {
        val builder = AlertDialog.Builder(requireActivity())
        val viewDialog = this.layoutInflater.inflate(R.layout.verification_otp_dialog, null)
        builder.setView(viewDialog)
        val alertDialog = builder.create()
        viewDialog.findViewById<TextView>(R.id.tvEmail).text = email
        viewDialog.findViewById<TextView>(R.id.textView2).text =
            if (type.contains("cell")) "Enter the 6-digit code from the phone number we just sent to" else "Enter the 6-digit code from the email we just sent to"
        val firstPinView = viewDialog.findViewById<PinView>(R.id.firstPinView)
        viewDialog.findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            alertDialog.dismiss()
            etEmailId.setText(profileData!!.secondary_email ?: "")
        }

        viewDialog.findViewById<Button>(R.id.btVerify).setOnClickListener {
            if (/*otp.isEmpty() && */firstPinView.text.toString().length == 6) {
//                alertDialog.dismiss()
                hideKeyboardDialog(alertDialog.currentFocus)
                onVerify(firstPinView.text.toString(), email)
//            } else if (otp.isNotEmpty() && otp.equals(firstPinView.text.toString())) {
//                alertDialog.dismiss()
//                onVerify(otp, email)
            } else toast("Wrong OTP", false)
        }

        alertDialog.setOnDismissListener {
            hideKeyboardDialog(alertDialog.currentFocus)
        }
        alertDialog.setCancelable(true)
        alertDialog.show()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window?.getAttributes())
        val dialogWindowWidth = (displayWidth * 0.9f).toInt()
        layoutParams.width = dialogWindowWidth
        alertDialog.window?.attributes = layoutParams
        return alertDialog
    }

    override fun onVerifySecondaryOTPResp(
        it: SignInWithUserDetailEntity,
        email: String,
        type: String
    ) {
        if (alertDialog != null) {
            if (alertDialog!!.isShowing) alertDialog!!.dismiss()
            hideAllTypeKB(requireView().windowToken)
        }
        if (it.success.not()) {
            if (type.equals("secondary_email"))
                etEmailId.setText(email)
            if (type.equals("secondary_cell_number"))
                etSecPhonenNo.setText(email)
        } else {
            if (type.equals("secondary_email"))
                etEmailId.setText(email)
            else if (type.equals("secondary_cell_number"))
                etPhonenNo.setText(email)
            else {
//                tvEmail.setText(email)
                try {
                    onLogout("")
                } catch (e: Exception) {
                }
            }
            toast(it.message)
        }
    }

    private fun updateSession() {
        val userdata = mAppUtils.getUserData(mPrefs)
        userdata!!.firstname = profileData!!.first_name
        userdata.lastname = profileData!!.last_name
        userdata.email = profileData!!.email
        userdata.address = profileData!!.address
        userdata.city = profileData!!.city
        userdata.state = profileData!!.state
        userdata.postalcode = profileData!!.postal_code.toString()
        mPrefs.setKeyValue(PreferenceConstants.USER_DATA, Gson().toJson(userdata))
    }


    override fun onProfileImageResponse(it: ProfileUpdatedEntity) {
        ivUser.loadImageRadius(it.data.pic_url ?: "")
    }

    private fun editableView(editable: Boolean) {
        tvEdit.text = if (editable) "Save" else "Edit"
        ivEditUserEmail.onOffVisibility(editable)
        ivEditUserSecPhone.onOffVisibility(editable)
        ivEditUserPhone.onOffVisibility(editable)
        ivEditUser.onOffVisibility(editable)
        ivEditUserSecondaryEmail.onOffVisibility(editable)
        tvfname.onOffVisibility(editable)
        tvlname.onOffVisibility(editable)
        etFname.onOffVisibility(editable)
        etLname.onOffVisibility(editable)
        etFname.isEnabled = editable
        etLname.isEnabled = editable
        spinnerGender.isEnabled = editable
        etPhonenNo.isEnabled = false
        etSecPhonenNo.isEnabled = false
        etEmailId.isEnabled = false
        etAptNumber.isEnabled = editable
        etCity.isEnabled = editable
        etState.isEnabled = editable
        etZipcode.isEnabled = editable
//        etCountry.isEnabled = editable
    }

    val mBuilder: Dialog by lazy { Dialog(requireActivity()) }
    fun showImagePop() {
        mBuilder.setContentView(R.layout.camera_dialog);
        mBuilder.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation;
        mBuilder.window!!.setGravity(Gravity.BOTTOM)
        mBuilder.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mBuilder.findViewById<TextView>(R.id.titleCamera)
            .setOnClickListener {
                mBuilder.dismiss()
                dispatchTakePictureIntent()
            }
        mBuilder.findViewById<TextView>(R.id.titleGallery)
            .setOnClickListener {
                mBuilder.dismiss()
                dispatchTakeGalleryIntent()
            }
        mBuilder.findViewById<TextView>(R.id.titleCancel)
            .setOnClickListener { mBuilder.dismiss() }
        mBuilder.show();
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    ConstUtils.createImageFile(requireActivity())
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireActivity(),
                        "${requireActivity().packageName}.provider",
                        it
                    )
                    picture_url = photoURI.toString()
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, ConstUtils.REQUEST_TAKE_PHOTO)
                }
            }

        }
    }

    private fun dispatchTakeGalleryIntent() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, ConstUtils.REQUEST_IMAGE_GET)
        }
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
            } else if (requestCode == ConstUtils.REQUEST_TAKE_PHOTO) {
                CropImage.activity(Uri.parse(picture_url))
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setAspectRatio(1, 1)
                    .setGuidelinesColor(android.R.color.transparent).start(requireActivity(), this)
            } else if (requestCode == ConstUtils.REQUEST_IMAGE_GET) {
                val uri: Uri = data?.data!!
                CropImage.activity(uri).setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setAspectRatio(1, 1)
                    .setGuidelinesColor(android.R.color.transparent).start(requireActivity(), this)
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    saveCaptureImageResults(result.uri)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                }
            }
        }
    }

    private fun saveCaptureImageResults(data: Uri) {
        try {
            val file = File(data.path!!)
            val compressedImageFile = Compressor(requireActivity())
                .setMaxHeight(800).setMaxWidth(800)
                .setQuality(90)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .compressToFile(file)
            if (isInternetConnected())
                presenter.editProfileImage(compressedImageFile.path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GlobalVariable.MULTI_LOC_STOR && permissions.isNotEmpty()) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                CommonUtil.permissionAlert(
                    requireActivity(),
                    getString(R.string.storage_permission_dialog)
                )
            else showImagePop()
        }
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


    fun setPlaceData(placeData: Place) {
        try {
            etAddressa.setText("")
            etCity.setText("")
//            etState.setText("")
            if (!placeData.getName()!!.toLowerCase(Locale.ENGLISH)
                    .isEmpty()
            ) etAddressa.setText(
                placeData.getName()
            )

            for (i in 0 until placeData.getAddressComponents()!!.asList().size) {
                val place: AddressComponent = placeData.getAddressComponents()!!.asList().get(i)
                if (place.types.contains("locality"))
                    etCity.setText(place.name)
//                if (place.types.contains("administrative_area_level_1"))
//                    etState.setText(place.name)
                if (place.types.contains("postal_code"))
                    etZipcode.setText(place.name)
                if (place.types.contains("street_number")) {
                    etAddressa.setText("${etAddressa.text} ${place.name}")
                }
//                if (place.types.contains("country")) {
//                    etCountry.setText("${place.name}")
//                }

            }
        } catch (e: java.lang.Exception) {
            Log.e("Exce......", e.toString())
        }

    }



    @RequiresApi(Build.VERSION_CODES.O)
    public fun dobPopup() {

//      val data = arrayOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP","OCT","NOV","DEC")
        val days = arrayOf("01", "02", "03", "04", "05", "06", "07", "08", "09","10","11","12",
            "13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31")
        val data = arrayOf("01", "02", "03", "04", "05", "06", "07", "08", "09","10","11","12")
        val builder = AlertDialog.Builder(requireActivity())
        val viewDialog = this.layoutInflater.inflate(R.layout.dialog_dob, null)
        builder.setView(viewDialog)
        val alertDialog = builder.create()

        var day=""
        var month=""
        var year=""
        viewDialog.findViewById<NumberPicker>(R.id.number_picker1).displayedValues=data
        viewDialog.findViewById<NumberPicker>(R.id.number_picker2).displayedValues=days
        viewDialog.findViewById<NumberPicker>(R.id.number_picker3)

        if (etDob.text.toString().isNotEmpty()){


            var month_current1 =   etDob.text.toString().substringBefore("-")
            var date_cut1 =   etDob.text.toString().substringAfter("-")
            var day_current1 =   date_cut1.toString().substringBefore("-")
            var year_current1 =   date_cut1.toString().substringAfter("-")

            day=day_current1
            month=month_current1
            year=year_current1


            if (month_current1.contains("0")){
                viewDialog.findViewById<NumberPicker>(R.id.number_picker1).value=month_current1.replace("0","").toInt()
            }else{
                viewDialog.findViewById<NumberPicker>(R.id.number_picker1).value=  month_current1.toInt()
            }

            if (day_current1.contains("0")){
                viewDialog.findViewById<NumberPicker>(R.id.number_picker2).value=day_current1.replace("0","").toInt()
            }else{
                viewDialog.findViewById<NumberPicker>(R.id.number_picker2).value=  day_current1.toInt()
            }

            viewDialog.findViewById<NumberPicker>(R.id.number_picker3).value=  year.toInt()
        }else{
            val current1 = LocalDateTime.now()
            val formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val formatted1 = current1.format(formatter1)

            var day_current1 =   formatted1.toString().substringBefore("/")
            var date_cut1 =   formatted1.toString().substringAfter("/")
            var month_current1 =   date_cut1.toString().substringBefore("/")

            day=day_current1
            month=month_current1
            year="1990"
            if (month_current1.contains("0")){
                viewDialog.findViewById<NumberPicker>(R.id.number_picker1).value=month_current1.replace("0","").toInt()
            }else{
                viewDialog.findViewById<NumberPicker>(R.id.number_picker1).value=  month_current1.toInt()
            }

            if (day_current1.contains("0")){
                viewDialog.findViewById<NumberPicker>(R.id.number_picker2).value=day_current1.replace("0","").toInt()
            }else{
                viewDialog.findViewById<NumberPicker>(R.id.number_picker2).value=  day_current1.toInt()
            }


        }



        // OnValueChangeListener
        // OnValueChangeListener
        viewDialog.findViewById<NumberPicker>(R.id.number_picker1).setOnValueChangedListener(
            NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->

                if (picker.value.toString().length==1){
                    month = "0"+picker.value.toString()
                }else{
                    month = picker.value.toString()
                }
            })

        viewDialog.findViewById<NumberPicker>(R.id.number_picker2).setOnValueChangedListener(
            NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->
                if (picker.value.toString().length==1){
                    day = "0"+picker.value.toString()
                }else{
                    day = picker.value.toString()
                }

            })

        viewDialog.findViewById<NumberPicker>(R.id.number_picker3).setOnValueChangedListener(
            NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->
                year = picker.value.toString()
            })

        viewDialog.findViewById<Button>(R.id.btnSave).setOnClickListener {

            if (day.isEmpty()) {

            } else if (month.isEmpty()) {

            } else if (year.isEmpty()) {

            }else {

                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val formatted = current.format(formatter)

                var dob = month + "-" + day + "-" + year.replace(",", "")
                val sdformat = SimpleDateFormat(formatted)

                var day_current =   formatted.toString().substringBefore("/")
                var date_cut =   formatted.toString().substringAfter("/")
                var month_current =   date_cut.toString().substringBefore("/")

                if (year.replace(",", "").toInt()==2003){
                    if (month > month_current) {
//                        if (day>day_current){
                        toast("DOB must be required 18 years old")
//                        }else{
//                            toast(dob)
//                        }
                    }else if (day>day_current){
                        toast("DOB must be required 18 years old")
                    }else{
                        etDob.setText(dob)
                        alertDialog.dismiss()
                    }
                }else{
                    etDob.setText(dob)
                    alertDialog.dismiss()
                }

            }
        }


        viewDialog.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            alertDialog.dismiss()
        }

        viewDialog.findViewById<ImageView>(R.id.ivClose).setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.setCancelable(true)
        alertDialog.show()

        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window?.getAttributes())
        val dialogWindowWidth = (displayWidth * 0.9f).toInt()
        layoutParams.width = dialogWindowWidth
        layoutParams.height = (displayMetrics.heightPixels * 0.9f).toInt()
        alertDialog.window?.attributes = layoutParams
    }
}

