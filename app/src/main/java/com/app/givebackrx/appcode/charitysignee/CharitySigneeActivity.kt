package com.app.givebackrx.appcode.charitysignee

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.GBRxApp
import com.app.givebackrx.R
import com.app.givebackrx.base.BaseActivity
import com.app.givebackrx.data.entity.PharmacyFilterEntity
import com.app.givebackrx.listeners.SingleListCLickListener
import com.app.givebackrx.utils.CommonUtil
import com.app.givebackrx.utils.ConstUtils
import com.app.givebackrx.utils.GlobalVariable
import com.app.givebackrx.utils.extension.onOffVisibility
import com.app.givebackrx.utils.extension.toast
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_charity_signee.*
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap


class CharitySigneeActivity : BaseActivity(), View.OnClickListener, ICharitySigneeView {

    companion object {
        private var picture_url = ""
        private var charityLogo = ""
        private var additionalLogo = ""
    }

    val categoryPopList = mutableListOf<PharmacyFilterEntity>()
    val calendar = Calendar.getInstance(Locale.ENGLISH)

    private var uploadType = 0
    private var categoryList = mutableListOf<PharmacyFilterEntity>()
    private val mCategoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter(categoryList) {

        }
    }
    private val statesList by lazy { GBRxApp().addStates(this) }
    private var arrayAdapter: ArrayAdapter<String>? = null

    @Inject
    lateinit var presenter: CharitySigneePresenter<ICharitySigneeView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charity_signee)
        presenter.onAttach(this)
        calendar.add(Calendar.YEAR, -18)
        rvCategory.adapter = mCategoryAdapter
        setClicks()
        setAutoCompleteAdapter()
        addPhoneFormat()
        categoryPopList.apply {
            add(PharmacyFilterEntity("Animals", "Animals", false,""))
            add(PharmacyFilterEntity("Arts,Culture,Humanities", "Arts,Culture,Humanities", false,""))
            add(PharmacyFilterEntity("Community Development", "Community Development", false,""))
            add(PharmacyFilterEntity("Education", "Education", false,""))
            add(PharmacyFilterEntity("Health", "Health", false,""))
            add(PharmacyFilterEntity("Human and Civil Rights", "Human and Civil Rights", false,""))
            add(PharmacyFilterEntity("Human Services", "Human Services", false,""))
            add(PharmacyFilterEntity("Other", "", false,""))
        }
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
        etMainPhone.filters = filter
        etPhonenNo.filters = filter
    }

    private fun setAutoCompleteAdapter() {
        arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            statesList.map { it.state }.toList()
        )
        spinnerStateFinancial.adapter = arrayAdapter
        etCharityState.adapter = arrayAdapter

        ////
        val autoAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            arrayOf(
                "Animals",
                "Arts,Culture,Humanities",
                "Community Development",
                "Education",
                "Health",
                "Human and Civil Rights",
                "Human Services",
                "Other"
            )
        )


    }

    private fun setClicks() {
        btnNext.setOnClickListener(this)
        tvUploadPhoto.setOnClickListener(this)
        tvAdditionalPhoto.setOnClickListener(this)
        etDob.setOnClickListener(this)
        etCharityCategory.setOnClickListener(this)
        ivSigneeInfo.setOnClickListener(this)
        ivCharityInfo.setOnClickListener(this)
        ivFinancialInfo.setOnClickListener(this)
        etFederalID.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            if (source.length > 0) {
                if (!Character.isDigit(source[0])) return@InputFilter "" else {
                    if (dstart == 1) {
                        return@InputFilter "$source-"
                    }
                }
            }
            null
        })

        spinnerPaymentMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                lblRounting.onOffVisibility(spinnerPaymentMethod.selectedItemPosition == 0)
                etRounting.onOffVisibility(spinnerPaymentMethod.selectedItemPosition == 0)
                lblAccountNum.onOffVisibility(spinnerPaymentMethod.selectedItemPosition == 0)
                etAccountNum.onOffVisibility(spinnerPaymentMethod.selectedItemPosition == 0)
            }
        }
    }

    override fun onClick(p0: View) {
        when (p0) {
            tvUploadPhoto -> {
                uploadType = 0
                if (permissionFile.checkLocStorgePermission(this@CharitySigneeActivity)) showImagePop()
            }
            etCharityCategory -> {
                showCategoryPop()
            }
            ivSigneeInfo -> {
                showInformationPop(
                    "You will be asked to complete the following screens:",
                    "Charity signeeinformation:\nPerson responsible for signing documents on behalf of the organization. This individual will also be assigned a username and password to log in to view donations generated by givebackRx."
                )
            }
            ivCharityInfo -> {
                showInformationPop(
                    "You will be asked to complete the following screens:",
                    "Charity information: Name, address, description of the charity, and logo are sample data required."
                )
            }
            ivFinancialInfo -> {
                showInformationPop(
                    "You will be asked to complete the following screens:",
                    "Charity financial information: Selected option for how the charity would like to be sent donations. if you don't have this information. That's okay. Click cancel and come back when you do!"
                )
            }
            tvAdditionalPhoto -> {
                uploadType = 1
                if (permissionFile.checkLocStorgePermission(this@CharitySigneeActivity)) showImagePop()
            }
            etDob -> {
                DatePickerDialog(
                    this@CharitySigneeActivity,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        etDob.text = /*mAppUtils.convertDateFormat(*/
                            "${if ((month + 1) > 9) month + 1 else "0${month + 1}"}-${if (dayOfMonth>9)dayOfMonth else "0$dayOfMonth"}-$year"
//                            "yyyy-MM-dd",
//                            "dd/MM/yyyy"
//                        )
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            btnNext -> {
                if (financialInfo.isChecked) {
                    if (validationThirdPage()) {
                        //callapi
                        if (isInternetConnected()) {
                            var categories = ""
                            categoryList.forEach {
                                if (it.selected)
                                    categories =
                                        if (categories.isEmpty()) it.value else "$categories;${it.value}"
                            }
                            presenter.charitySignee(signeeJson(categories))
                        }
                    }
                } else if (charityInfo.isChecked) {
                    if (validationSecondPage()) {
                        btnNext.text = "Submit"
                        financialInfo.isChecked = true
                        financialInfoLayout.onOffVisibility(true)
                        charityInfoLayout.onOffVisibility(false)
                        signeeInfoLayout.onOffVisibility(false)
                    }
                } else {
                    if (validationFirstPage()) {
                        charityInfo.isChecked = true
                        charityInfoLayout.onOffVisibility(true)
                        financialInfoLayout.onOffVisibility(false)
                        signeeInfoLayout.onOffVisibility(false)
                    }
                }
            }
        }
    }


    val mBuilder: Dialog by lazy { Dialog(this) }
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

    val mCategoryBuilder: Dialog by lazy { Dialog(this) }
    fun showCategoryPop() {
        mCategoryBuilder.setContentView(R.layout.categories_dialog);
        mCategoryBuilder.window!!.setBackgroundDrawableResource(R.drawable.shadow_border_bg_more_rounded)
          val displayMetrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getMetrics(displayMetrics)
               mCategoryBuilder.window!!.setLayout(
            (displayMetrics.widthPixels * 0.95f).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mCategoryBuilder.findViewById<ImageView>(R.id.ivClose).setOnClickListener { mCategoryBuilder.dismiss() }
        val rvCategory = mCategoryBuilder.findViewById<RecyclerView>(R.id.rvCategory)
        val tvCharityOtherCat = mCategoryBuilder.findViewById<TextView>(R.id.tvCharityOtherCat)
        val etCharityOtherCat = mCategoryBuilder.findViewById<EditText>(R.id.etCharityOtherCat)
        val adapter = CategoryPopAdapter(categoryPopList, object : SingleListCLickListener {
            override fun onSingleListClick(item: Any, position: Int) {
                if (categoryPopList[position].label.equals("Other")) {
                    tvCharityOtherCat.onOffVisibility(categoryPopList[position].selected)
                    etCharityOtherCat.onOffVisibility(categoryPopList[position].selected)
                    etCharityOtherCat.setText(categoryPopList[position].value)
                }
            }
        })
        categoryPopList.forEachIndexed { index, data ->
            if (data.selected&&data.label.equals("Other")) {
                tvCharityOtherCat.onOffVisibility(true)
                etCharityOtherCat.onOffVisibility(true)
                etCharityOtherCat.setText(data.value)
            }
        }
        rvCategory.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        rvCategory.adapter = adapter
        mCategoryBuilder.findViewById<TextView>(R.id.tvDone)
            .setOnClickListener {
                if (tvCharityOtherCat.visibility == View.VISIBLE && etCharityOtherCat.text.isEmpty()) {
                    toast("Enter Other Category", false)
                } else {
                    categoryList.clear()
                    categoryPopList.forEach {
                        if (it.selected) {
                            if (it.label.equals("Other"))
                                it.value = etCharityOtherCat.text.toString()
                            categoryList.add(it)
                        }
                    }
                    mCategoryAdapter.notifyDataSetChanged()
                    mCategoryBuilder.dismiss()
                }
            }
        mCategoryBuilder.show();
    }

    val mInfoBuilder: Dialog by lazy { Dialog(this) }
    fun showInformationPop(ttl: String = "", desc: String) {
        mInfoBuilder.setContentView(R.layout.charitysignee_info_dialog);
        val displayMetrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        mInfoBuilder.window!!.setBackgroundDrawableResource(R.drawable.shadow_border_bg_more_rounded)
        mInfoBuilder.window!!.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
            (displayMetrics.widthPixels * 0.7).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mInfoBuilder.findViewById<TextView>(R.id.titleHeader).apply {
            text = ttl
            onOffVisibility(ttl.isEmpty())
        }
        mInfoBuilder.findViewById<TextView>(R.id.tvDesc).text = desc

        mInfoBuilder.findViewById<TextView>(R.id.tvDone)
            .setOnClickListener {
                mInfoBuilder.dismiss()
            }
        mInfoBuilder.show();
    }

    fun validationFirstPage(): Boolean {
        var valid = false
        if (etFname.text.isEmpty()) {
            toast("Enter First Name", false)
        } else if (etLname.text.isEmpty()) {
            toast("Enter Last Name", false)
        } else if (etPhonenNo.text.isEmpty()) {
            toast("Enter Phone Number", false)
        } else if (etEmailId.text.isEmpty()) {
            toast("Enter Email Address", false)
        } else if (Patterns.EMAIL_ADDRESS.matcher(etEmailId.text.toString()).matches().not()) {
            toast("Enter Valid Email Address", false)
        } else if (etDob.text.isEmpty()) {
            toast("Enter Date Of Birth", false)
        } else if (etOrganizational.text.isEmpty()) {
            toast("Enter Organizational Role", false)
        } else if (etPassword.text.isEmpty()) {
            toast("Enter Current Password", false)
        } else if (etPassword.text.toString().equals(etCPassword.text.toString()).not()) {
            toast("Password does not match", false)
        } else valid = true

        return valid
    }

    fun validationSecondPage(): Boolean {
        var valid = false
        if (etCharityName.text.isEmpty()) {
            toast("Enter Charity Name", false)
        } else if (etAptSuite.text.isEmpty()) {
            toast("Enter Apt Suite", false)
        } else if (etCharityAddress.text.isEmpty()) {
            toast("Enter Charity Address", false)
        } else if (etCharityCity.text.isEmpty()) {
            toast("Enter Charity City", false)
        }
//        else if (etCharityState.text.isEmpty()){ toast("Enter Last Name",false)}
        else if (etCharityCountry.text.isEmpty()) {
            toast("Enter Charity Country", false)
        } else if (etCharityZIP.text.isEmpty()) {
            toast("Enter Charity Postal Code", false)
        } else if (etMainPhone.text.isEmpty()) {
            toast("Enter Phone Number", false)
        } else if (etCharityWebsite.text.isEmpty()) {
            toast("Enter Charity Website", false)
        } else if (etFederalID.text.isEmpty()) {
            toast("Enter Federal ID", false)
        }
//        else if (etStatus501.text.isEmpty()){ toast("Enter Email Address",false)}
        else if (categoryList.isEmpty()) {
            toast("Choose Category your charity falls under", false)
        } else if (etUploadPhoto.text.isEmpty() || charityLogo.isEmpty()) {
            toast("Choose Upload Photo", false)
        } else if (etAdditionalPhoto.text.isEmpty() || additionalLogo.isEmpty()) {
            toast("Choose Additional Photo", false)
        } else if (etShortDesc.text.isEmpty()) {
            toast("Enter Short Description", false)
        } else valid = true

        return valid
    }

    fun validationThirdPage(): Boolean {
        var valid = false
        if (spinnerPaymentMethod.selectedItemPosition == 0 && etRounting.text.isEmpty()) {
            toast("Enter Rounting Number", false)
        } else if (spinnerPaymentMethod.selectedItemPosition == 0 && etAccountNum.text.isEmpty()) {
            toast("Enter Account Number", false)
        } else if (etAddressFinancial.text.isEmpty()) {
            toast("Enter Financial Address", false)
        } else if (etCityFinancial.text.isEmpty()) {
            toast("Enter Financial City", false)
        }
//         if (spinnerStateFinancial.text.isEmpty()){ toast("Enter Charity Postal Code",false)}
        else if (etZipFinancial.text.isEmpty()) {
            toast("Enter Financial Postal Code", false)
        }
        valid = true

        return valid
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(this.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    ConstUtils.createImageFile(this)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "${this.packageName}.provider",
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
        if (intent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(intent, ConstUtils.REQUEST_IMAGE_GET)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == ConstUtils.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
//                try {
//                    val place = Autocomplete.getPlaceFromIntent(data!!)
//                    setPlaceData(place)
//                } catch (e: java.lang.Exception) {
//                }
            /*} else */if (requestCode == ConstUtils.REQUEST_TAKE_PHOTO) {
                CropImage.activity(Uri.parse(picture_url))
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setAspectRatio(1, 1)
                    .setGuidelinesColor(android.R.color.transparent)
                    .start(this@CharitySigneeActivity)
            } else if (requestCode == ConstUtils.REQUEST_IMAGE_GET) {
                val uri: Uri = data?.data!!
                CropImage.activity(uri).setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setAspectRatio(1, 1)
                    .setGuidelinesColor(android.R.color.transparent)
                    .start(this@CharitySigneeActivity)
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
            val compressedImageFile = Compressor(this)
                .setMaxHeight(800).setMaxWidth(800)
                .setQuality(90)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .compressToFile(file)
            if (uploadType == 0) {
                charityLogo = compressedImageFile.path ?: compressedImageFile.absolutePath
                etUploadPhoto.text = compressedImageFile.name.replace("cropped", "")
            } else {
                additionalLogo = compressedImageFile.path ?: compressedImageFile.absolutePath
                etAdditionalPhoto.text = compressedImageFile.name.replace("cropped", "")
            }
//            if (isInternetConnected())
//                presenter.editProfileImage(compressedImageFile.path)
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
                    this,
                    getString(R.string.storage_permission_dialog)
                )
            else showImagePop()
        }
    }

    override fun onGeneratedToken(lastAction: String) {

    }


    override fun onBackPressed() {
        if (financialInfo.isChecked) {
            btnNext.text = "Next"
            financialInfo.isChecked = false
            charityInfoLayout.onOffVisibility(true)
            financialInfoLayout.onOffVisibility(false)
            signeeInfoLayout.onOffVisibility(false)
            //callapi
        } else if (charityInfo.isChecked) {
            btnNext.text = "Next"
            charityInfo.isChecked = false
            financialInfoLayout.onOffVisibility(false)
            charityInfoLayout.onOffVisibility(false)
            signeeInfoLayout.onOffVisibility(true)
        } else {
            super.onBackPressed()
        }

    }

    /*charitySignup(signee_first_name:String,signee_last_name:String,signee_phone:String,signee_email:String,signee_dob:String,signee_gender:String,signee_organizational_role:String
    ,signee_password:String,charity_name:String,charity_apt_suite_number:String,charity_address:String,charity_city:String,charity_state:String,charity_country:String,
    charity_zip_code:String,charity_main_phone:String,website:String,federal_id_number:String,status_501:String,category:String,charity_logo:Data,additional_image:Data
    ,description:String,preferred_payment_method:String,charity_address_cheque:String,charity_city_cheque:String,charity_state_cheque:String,charity_zip_code_cheque:String)*/
    fun signeeJson(categories: String) = HashMap<String, RequestBody>().apply {
        put(
            "signee_first_name",
            RequestBody.create(MediaType.parse("text/plain"), etFname.text.toString())
        )
        put(
            "signee_last_name",
            RequestBody.create(MediaType.parse("text/plain"), etLname.text.toString())
        )
        put(
            "signee_organizational_role",
            RequestBody.create(MediaType.parse("text/plain"), etOrganizational.text.toString())
        )
        put(
            "signee_phone",
            RequestBody.create(MediaType.parse("text/plain"), etPhonenNo.text.toString())
        )
        put(
            "signee_email",
            RequestBody.create(MediaType.parse("text/plain"), etEmailId.text.toString())
        )
        put(
            "signee_dob",
            RequestBody.create(MediaType.parse("text/plain"), etDob.text.toString())
        )
        put(
            "signee_password",
            RequestBody.create(MediaType.parse("text/plain"), etPassword.text.toString())
        )
        put(
            "signee_gender",
            RequestBody.create(MediaType.parse("text/plain"), spinnerGender.selectedItem.toString())
        )
        put(
            "charity_apt_suite_number",
            RequestBody.create(MediaType.parse("text/plain"), etAptSuite.text.toString())
        )
        put(
            "charity_name",
            RequestBody.create(MediaType.parse("text/plain"), etCharityName.text.toString())
        )
        put(
            "charity_address",
            RequestBody.create(MediaType.parse("text/plain"), etCharityAddress.text.toString())
        )
        put(
            "charity_city",
            RequestBody.create(MediaType.parse("text/plain"), etCharityCity.text.toString())
        )
        put(
            "charity_state",
            RequestBody.create(
                MediaType.parse("text/plain"),
                statesList[etCharityState.selectedItemPosition].code.toString()
            )
        )
        put(
            "charity_country",
            RequestBody.create(MediaType.parse("text/plain"), etCharityCountry.text.toString())
        )
        put(
            "charity_zip_code",
            RequestBody.create(MediaType.parse("text/plain"), etCharityZIP.text.toString())
        )
        put(
            "charity_address_cheque",
            RequestBody.create(MediaType.parse("text/plain"), etCharityAddress.text.toString())
        )
        put(
            "charity_city_cheque",
            RequestBody.create(MediaType.parse("text/plain"), etCharityCity.text.toString())
        )
        put(
            "charity_state_cheque",
            RequestBody.create(
                MediaType.parse("text/plain"),
                statesList[etCharityState.selectedItemPosition].code.toString()
            )
        )
        put(
            "charity_zip_code_cheque",
            RequestBody.create(MediaType.parse("text/plain"), etCharityZIP.text.toString())
        )
        put(
            "charity_phone",
            RequestBody.create(MediaType.parse("text/plain"), etMainPhone.text.toString())
        )
        put(
            "image\"; filename=\"charity_logo.jpeg",
            RequestBody.create(MediaType.parse("image/jpeg"), File(charityLogo))
        )
        put(
            "image\"; filename=\"additional_image.jpeg",
            RequestBody.create(MediaType.parse("image/jpeg"), File(additionalLogo))
        )
        put(
            "website",
            RequestBody.create(MediaType.parse("text/plain"), etCharityWebsite.text.toString())
        )
        put(
            "category",
            RequestBody.create(MediaType.parse("text/plain"), categories)
        )
        put(
            "federal_id_number",
            RequestBody.create(MediaType.parse("text/plain"), etFederalID.text.toString())
        )
        put(
            "status_501",
            RequestBody.create(MediaType.parse("text/plain"), etStatus501.selectedItem.toString())
        )
        put(
            "description",
            RequestBody.create(MediaType.parse("text/plain"), etShortDesc.text.toString())
        )
        put(
            "preferred_payment_method",
            RequestBody.create(
                MediaType.parse("text/plain"),
                spinnerPaymentMethod.selectedItem.toString()
            )
        )
        put(
            "charity_poc_name",
            RequestBody.create(MediaType.parse("text/plain"), etAddressFinancial.text.toString())
        )
        put(
            "charity_poc_number",
            RequestBody.create(MediaType.parse("text/plain"), etCityFinancial.text.toString())
        )
        put(
            "type",
            RequestBody.create(MediaType.parse("text/plain"), "charityUser")
        )
        put(
            "charity_main_phone",
            RequestBody.create(MediaType.parse("text/plain"), etZipFinancial.text.toString())
        )
    }

}