package com.app.givebackrx.appcode.signupModule

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.app.givebackrx.GBRxApp
import com.app.givebackrx.R
import com.app.givebackrx.appcode.loginEmailPaswordModule.LoginEmailPasswordActivity
import com.app.givebackrx.appcode.selectCharityModule.SelectCharityActivity
import com.app.givebackrx.appcode.verifyOtpModule.VerifyOTPActivity
import com.app.givebackrx.appcode.web.WebActivity
import com.app.givebackrx.base.BaseActivity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.utils.AppConstants
import com.app.givebackrx.utils.AppConstants.PATTERN
import com.app.givebackrx.utils.extension.toast
import com.bruce.pickerview.popwindow.DatePickerPopWin
import com.bruce.pickerview.popwindow.DatePickerPopWin.Builder
import com.google.android.libraries.places.api.model.AddressComponent
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.activity_pre_sign_up_check.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.tvSignIn
import kotlinx.android.synthetic.main.dialog_dob.*
import kotlinx.android.synthetic.main.fragment_store_checkout.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


class SignUpActivity : BaseActivity(), View.OnClickListener, ISignupView {

    val email_extra by lazy { intent.getStringExtra("email_extra") ?: "" }
    val phone_extra by lazy { intent.getStringExtra("phone_extra") ?: "" }

    @Inject
    lateinit var presenter: SignupPresenter<ISignupView>


    val calendar: Calendar by lazy { Calendar.getInstance(Locale.ENGLISH) }

    val statesList by lazy { GBRxApp().addStates(this) }
    var arrayAdapter: ArrayAdapter<String>? = null

    var otptype = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        presenter.onAttach(this)
        calendar.add(Calendar.YEAR, -18)


        arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            statesList.map { it.state }.toList()
        )
        etState.adapter = arrayAdapter

        ivback.setOnClickListener(this)
        tvSignIn.setOnClickListener(this)
        etAddressa.setOnClickListener(this)
        etDob.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        etEmailId.setText(email_extra) // from intent
        if(email_extra.isNotEmpty()){
            radioGroup.check(R.id.radioemail)
            otptype = "email"
        }


        if(phone_extra.isNotEmpty()){
            radioGroup.check(R.id.radiophone)
            otptype = "phone"
            tvterms.visibility = View.VISIBLE
            checkAgree.visibility = View.VISIBLE
        }
        etPhonenNo.setText(phone_extra) // from intent
        etPhonenNo.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
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


        val spannableString = SpannableString(tvterms.text)
        val click = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(
                    Intent(this@SignUpActivity, WebActivity::class.java).putExtra(
                        "name",
                        "Terms of Service"
                    ).putExtra("url", "https://www.givebackrx.com/terms_and_condition")
                )
            }
        }
        val click1 = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(
                    Intent(this@SignUpActivity, WebActivity::class.java).putExtra(
                        "name",
                        "Privacy Policy"
                    ).putExtra("url", "https://www.givebackrx.com/privacyPolicy")
                )
            }

        }
        spannableString.setSpan(click, 66, 86, 0)
        spannableString.setSpan(click1, 88, 102, 0)
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            135,
            139,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(ForegroundColorSpan(Color.BLUE), 66, 86, 0)
        spannableString.setSpan(ForegroundColorSpan(Color.BLUE), 88, 102, 0)
        tvterms.text = spannableString
        tvterms.setMovementMethod(LinkMovementMethod.getInstance());

        //Switching event
        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.radiophone -> {
                    otptype = "phone"
                    tvterms.visibility = View.VISIBLE
                    checkAgree.visibility = View.VISIBLE
                }
                R.id.radioemail -> {
                    otptype = "email"
                    tvterms.visibility = View.GONE
                    checkAgree.visibility = View.GONE
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivback -> finish()
            R.id.tvSignIn -> {
                if (intent.hasExtra("sixforone")) {
                    startActivity(
                        Intent(this, LoginEmailPasswordActivity::class.java).putExtra(
                            "sixforone",
                            "true"
                        )
                    )
                } else {
                    startActivity(Intent(this, LoginEmailPasswordActivity::class.java))
                }
            }
            R.id.etDob -> {

                dobPopup()

//                datePicker()


//                val c = Calendar.getInstance()
//                var mYear = c[Calendar.YEAR]
//                var mMonth = c[Calendar.MONTH]
//                var mDay = c[Calendar.DAY_OF_MONTH]
//                val datePickerDialog = DatePickerDialog(this,
//                        { view, year, monthOfYear, dayOfMonth -> etDob.setText(String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth)) }, mYear, mMonth, mDay)
//
//                datePickerDialog.show()

//                DatePickerDialog(
//                    this@SignUpActivity,
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
            R.id.etAddressa -> openPlaceDialog()
            R.id.btnNext -> {


                if (etFname.text.toString().trim().isEmpty()) {
                    toast("Enter first name", false)
                } else if (etLname.text.toString().trim().isEmpty()) {
                    toast("Enter last name", false)
                } else if (etPhonenNo.text.toString().trim().isEmpty()) {
                    toast("Enter phone number", false)
                } else if (etPhonenNo.text.toString().trim().length < 14) {
                    toast("Enter valid phone number", false)
                } else if (etEmailId.text.toString().trim().isEmpty()) {
                    toast("Enter email", false)
                } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmailId.text.toString().trim())
                        .matches()
                ) {
                    toast("Enter valid email", false)
                } else if (etDob.text.toString().trim().isEmpty()) {
                    toast("Select Date of birth", false)
                }  /*else if (etAddressa.text.toString().trim().isEmpty()) {
                    toast("Enter address", false)
                } else if (etAddress.text.toString().trim().isEmpty()) {
                    toast("Enter address", false)
                } else if (etCity.text.toString().trim().isEmpty()) {
                    toast("Enter city", false)
//                } else if (etState.text.toString().trim().isEmpty()) {
//                    toast("Enter State", false)
                } else if (etCountry.text.toString().trim().isEmpty()) {
                    toast("Enter country", false)
                } else if (etZipcode.text.toString().trim().isEmpty()) {
                    toast("Enter ZIP code", false)
                } */ else if (etPassword.text.toString().trim().isEmpty()) {
                    toast("Enter password", false)
                } else if (!PATTERN.matcher(etPassword.text.toString().trim()).matches()) {
                    toast(
                        "Password must contain upper and lower letters, numerics, and a special character with a min of 8 characters",
                        false
                    )
                } else if (etCPassword.text.toString().trim().isEmpty()) {
                    toast("Confirm password", false)
                } else if (etCPassword.text.toString().trim()
                        .equals(etPassword.text.toString().trim()).not()
                ) {
                    toast("Password does not match", false)
                } else if (otptype.isEmpty()) {
                    toast("Select verification code receiving option", false)
                } else if (checkAgree.visibility == View.VISIBLE && checkAgree.isChecked.not()) {
                    toast("Please accept phone number terms and conditions", false)
                } else {
                    callSignup()
//                    applySignup()
//                    policyPopup()
                }
            }
        }
    }

    private fun datePicker() {
        val pickerPopWin: DatePickerPopWin =
            Builder(this@SignUpActivity,
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
        pickerPopWin.setSelectedDate("2003-01-01")
        pickerPopWin.showPopWin(this@SignUpActivity)
    }

    //    fun isValidPassword(password: String): Boolean {
//        val pattern: Pattern
//        val matcher: Matcher
//        val PASSWORD_PATTERN =
//            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
//        pattern = Pattern.compile(PASSWORD_PATTERN)
//        matcher = pattern.matcher(password)
//        return matcher.matches()
//    }
    private class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url);
            return true;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public fun policyPopup() {
        val builder = AlertDialog.Builder(this)
        val viewDialog = this.layoutInflater.inflate(R.layout.custom_policy_dialog, null)
        builder.setView(viewDialog)
        val alertDialog = builder.create()

        viewDialog.findViewById<TextView>(R.id.tvHeading).text = getString(R.string.privacy_policy)
        val cbTerms = viewDialog.findViewById<CheckBox>(R.id.cbTerms)
        viewDialog.findViewById<WebView>(R.id.tvMessage).apply {
            settings.javaScriptEnabled = true
            setWebViewClient(MyWebViewClient())
            loadUrl("https://givebackrx-gbe-marketplace.herokuapp.com/hippa_nopp")
//            loadUrl("https://givebackrx-marketplace-prod.herokuapp.com/privacyPolicy")
        }

        viewDialog.findViewById<TextView>(R.id.btnConfirm).setOnClickListener {
            if (cbTerms.isChecked) {
                applySignup()
            } else {
                toast("Authorize Terms and Condition", false)
            }
        }

        alertDialog.setCancelable(true)
        alertDialog.show()

        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val displayMetrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window?.getAttributes())
        val dialogWindowWidth = (displayWidth * 0.9f).toInt()
        layoutParams.width = dialogWindowWidth
        layoutParams.height = (displayMetrics.heightPixels * 0.9f).toInt()
        alertDialog.window?.attributes = layoutParams
    }

    private fun applySignup() {
        if (isInternetConnected()) {
            presenter.postSignUpVerification(
                etPhonenNo.text.toString().trim().replace(Regex("[()-]"), "")
                    .replace(" ", ""),
                etEmailId.text.toString().trim(),
                etReferralCode.text.toString().trim()
            )
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
            .build(this@SignUpActivity)
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
            etAddressa.setText("")
            etAddress.setText("")
            etCity.setText("")
//            etState.setText("")
            if (!placeData.getName()!!.toLowerCase(Locale.ENGLISH).isEmpty()){
                etAddressa.setText(placeData.getName())
                etAddress.visibility=View.VISIBLE
                etAddressa.visibility=View.INVISIBLE
                etAddress.setText(placeData.getName())
            }

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


    override fun onPostSignUpVerification(it: SuccessMessageEntity) {
        if (it.data != null) {
            if (it.data.next_page) {
                if (intent.hasExtra("sixforone")){
                    SelectCharityActivity.signup_extra = signupJson()
                    startActivity(
                        Intent(this@SignUpActivity, SelectCharityActivity::class.java).putExtra(
                            "sixforone",
                            "true"
                        )
                    )
                }else{
                    SelectCharityActivity.signup_extra = signupJson()
                    startActivity(Intent(this@SignUpActivity, SelectCharityActivity::class.java))
                }



            } else callSignup()
        } else callSignup()
    }

    fun callSignup() {
        if (isInternetConnected()) {
            presenter.signup(signupJson())
        }
    }

    private fun signupJson() = HashMap<String, Any>().apply {
        put("first_name", etFname.text.toString().trim())
        put("last_name", etLname.text.toString().trim())
        put(
            "phone",
            etPhonenNo.text.toString().trim().replace(Regex("[()-]"), "").replace(" ", "")
        )
        put("email", etEmailId.text.toString().trim())
        put("address", etAddress.text.toString().trim())
        put("city", etCity.text.toString().trim())
        put("postal_code", etZipcode.text.toString().trim())
        put("password", etPassword.text.toString().trim())
        put("dob", etDob.text.toString().trim())
        put("country", "US")
        put("gender", spinnerGender.selectedItem.toString())
        put("confirm_password", etCPassword.text.toString().trim())
        put("referral_code", etReferralCode.text.toString().trim())
        put("type", if (radioemail.isChecked) "email" else "phone")
        put("charity_id", "")
        put("state", statesList.get(etState.selectedItemPosition).code.trim())
        put("apt_suite_number", etAptNumber.text.toString().trim())
        put("next_page", false)
    }

    override fun onSignupResponse(it: SignInWithUserDetailEntity) {

        if (intent.hasExtra("sixforone")){

            Intent(this@SignUpActivity, VerifyOTPActivity::class.java).apply {
                putExtra("value_extra", etEmailId.text.toString().trim())
                putExtra("type_extra", "email")
                putExtra("page_extra", "signUp")
                putExtra("otp_extra", it.data.otp.toString())
                putExtra("sixforone", "true")
                putExtra("signup", "true")
                startActivity(this)
            }
        }else if (intent.hasExtra("page")){

            Intent(this@SignUpActivity, VerifyOTPActivity::class.java).apply {
                putExtra("value_extra", etEmailId.text.toString().trim())
                putExtra("type_extra", "email")
                putExtra("page_extra", "signUp")
                putExtra("otp_extra", it.data.otp.toString())
                putExtra("page", "cart")
                putExtra("signup", "true")
                startActivity(this)
            }
        }else{
            Intent(this@SignUpActivity, VerifyOTPActivity::class.java).apply {
                putExtra("value_extra", etEmailId.text.toString().trim())
                putExtra("type_extra", "email")
                putExtra("page_extra", "signUp")
                putExtra("otp_extra", it.data.otp.toString())
                putExtra("signup", "true")
                startActivity(this)
            }
        }




    }

    override fun onGeneratedToken(lastAction: String) {

    }


    @RequiresApi(Build.VERSION_CODES.O)
    public fun dobPopup() {

//      val data = arrayOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP","OCT","NOV","DEC")
        val days = arrayOf("01", "02", "03", "04", "05", "06", "07", "08", "09","10","11","12",
            "13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31")
        val data = arrayOf("01", "02", "03", "04", "05", "06", "07", "08", "09","10","11","12")
        val builder = AlertDialog.Builder(this)
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
        windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window?.getAttributes())
        val dialogWindowWidth = (displayWidth * 0.9f).toInt()
        layoutParams.width = dialogWindowWidth
        layoutParams.height = (displayMetrics.heightPixels * 0.9f).toInt()
        alertDialog.window?.attributes = layoutParams
    }
}
