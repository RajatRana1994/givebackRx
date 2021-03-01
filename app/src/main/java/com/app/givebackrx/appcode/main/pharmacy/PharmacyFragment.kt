package com.app.givebackrx.appcode.main.pharmacy

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.GBRxApp
import com.app.givebackrx.R
import com.app.givebackrx.appcode.drugDetail.DrugDetailFragment
import com.app.givebackrx.appcode.loginEmailPaswordModule.LoginEmailPasswordActivity
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.payment.PaymentFragment
import com.app.givebackrx.appcode.preSignUpModule.PreSignUpCheckActivity
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.*
import com.app.givebackrx.listeners.PairListCLickListener
import com.app.givebackrx.listeners.SingleListCLickListener
import com.app.givebackrx.utils.*
import com.app.givebackrx.utils.extension.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_pharmacy.*
import java.util.*
import javax.inject.Inject


class PharmacyFragment : BaseFragment(), IPharmacyView, SingleListCLickListener,
    PairListCLickListener, View.OnClickListener, OnMapReadyCallback,
    GoogleMap.OnInfoWindowClickListener, GoogleMap.InfoWindowAdapter {
    var cutomeQty = ""
    var edtQty: EditText? = null
    var btnQty: TextView? = null
    val mFilterAdapter by lazy { PharmacyFilterAdapter(mPharmacyBrands, this) }
    var selectedPositionForMap: Int = -1
    var bSheet: BottomSheetDialog? = null
    private var mMap: GoogleMap? = null
    var mMapFragment: SupportMapFragment? = null

    var needToClear = true
    val mPharmacyBrands by lazy { mutableListOf<PharmacyFilterEntity>() }
    val mPharmacyForms by lazy { mutableListOf<PharmacyFilterEntity>() }
    val mPharmacyDosages by lazy { mutableListOf<PharmacyFilterEntity>() }
    val mPharmacyQuantities by lazy { mutableListOf<PharmacyFilterEntity>() }
    val mPharmacyList by lazy { mutableListOf<PharmacyData>() }
    val mdrugInfo by lazy { mutableListOf<DrugInfo>() }
    val mdrugImages by lazy { mutableListOf<DrugImages>() }
    val mPharmacyAdapter by lazy { PharmacyAdapter(mPharmacyList, this, mPrefs) }
    val mPharmacyImagesAdapter by lazy { PharmacyImagesAdapter(mdrugImages, this, mPrefs) }

    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }

    var rootView: View? = null

    @Inject
    lateinit var presenter: PharmacyPresenter<IPharmacyView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView != null) {
            (rootView!!.parent as ViewGroup).removeView(rootView)
        }
        rootView = inflater.inflate(R.layout.fragment_pharmacy, container, false)
        try {
            mMapFragment =
                this.childFragmentManager.findFragmentById(R.id.fl_map) as SupportMapFragment
            MapsInitializer.initialize(requireActivity())
        } catch (e: Exception) {
        }
        return rootView
    }


    override fun setUp(view: View) {
        presenter.onAttach(this)
        resetAPIValues()

        if (mPrefs.getKeyValue(PreferenceConstants.LAT).isNotEmpty()) {
            latitude = mPrefs.getKeyValue(PreferenceConstants.LAT)
            longitude = mPrefs.getKeyValue(PreferenceConstants.LNG)
        }
        hideKeyboard()
        tvdiscountNear.text = SpannableString(tvdiscountNear.text.toString()).apply {
            spanBold(13, tvdiscountNear.text.toString().length)
        }
        rvPharmacy.adapter = mPharmacyAdapter
        rvImages.layoutManager= GridLayoutManager(context, 2)
        rvImages.adapter = mPharmacyImagesAdapter
//        callAPI(true)
        tvFilter.setOnSingleClickListener(this)
        ivBack.setOnClickListener(this)
        tvSortPrice.setOnClickListener(this)
        mTvCoupon.setOnClickListener(this)
        mTvDrugInfo.setOnClickListener(this)
        mTvBasics.setOnClickListener(this)
        mTvSideEffects.setOnClickListener(this)
        mTvImages.setOnClickListener(this)
        tvSortDistance.setOnSingleClickListener(this)
        tvMapView.setOnSingleClickListener(this)
        tvSearch.setOnSingleClickListener(this)
        ivInfo.setOnSingleClickListener(this)
        ivFav.setOnSingleClickListener(this)
        // call api

        try {
            if (requireArguments().containsKey("fav_extra") && GBRxApp.mSavePharmacyFavData != null) {
                drug_generic_name = GBRxApp.mSavePharmacyFavData!!.drug_generic_name
                drug_name = GBRxApp.mSavePharmacyFavData!!.drug_name
                is_location = GBRxApp.mSavePharmacyFavData!!.is_location
                from_drug_search = GBRxApp.mSavePharmacyFavData!!.from_drug_search
                form = GBRxApp.mSavePharmacyFavData!!.form
                dosage = GBRxApp.mSavePharmacyFavData!!.dosage
                quantity = GBRxApp.mSavePharmacyFavData!!.quantity
                sort_type = GBRxApp.mSavePharmacyFavData!!.sort_type
                latitude = GBRxApp.mSavePharmacyFavData!!.latitude
                longitude = GBRxApp.mSavePharmacyFavData!!.longitude
            }
        } catch (e: Exception) {
        }
        if (mPrefs.getKeyValue(PreferenceConstants.LAT).isNotEmpty()) {
            is_location = "true"
            sort_type = "Distance"
            latitude = mPrefs.getKeyValue(PreferenceConstants.LAT)
            longitude = mPrefs.getKeyValue(PreferenceConstants.LNG)
            callAPI()
            tvSortPrice.isChecked = true
            tvSortDistance.isChecked = false
            tvFilter.isChecked = false
        } else {
            sort_type = "Price"
            is_location = "false"
            callAPI(true)
            tvSortPrice.isChecked = true
            tvSortDistance.isChecked = false
            tvFilter.isChecked = false
        }

//        if (permissionFile.checklocationPermissions(requireActivity())) {
//            if (mPrefs.getKeyValue(PreferenceConstants.LAT).isNotEmpty()) {
//                is_location = "true"
//                sort_type = "Distance"
//                latitude = mPrefs.getKeyValue(PreferenceConstants.LAT)
//                longitude = mPrefs.getKeyValue(PreferenceConstants.LNG)
//                callAPI()
//            } else {
//                startLocationUpdates {
//                    sort_type = "Distance"
//                    is_location = "true"
//                    callAPI()
//                }
//            }
//            tvSortPrice.isChecked = false
//            tvSortDistance.isChecked = true
//            tvFilter.isChecked = false
//        } else {
//            sort_type = "Price"
//            is_location = "false"
//            from_drug_search = "false"
//            callAPI(true)
//            tvSortPrice.isChecked = true
//            tvSortDistance.isChecked = false
//            tvFilter.isChecked = false
//        }

    }


    val mInfoBuilder: Dialog by lazy { Dialog(requireActivity()) }
    fun showInformationPop(ttl: String = "", desc: String) {
        mInfoBuilder.setContentView(R.layout.charitysignee_info_dialog);
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        mInfoBuilder.window!!.setBackgroundDrawableResource(R.drawable.shadow_border_bg_more_rounded)
        mInfoBuilder.window!!.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
            (displayMetrics.widthPixels * 0.99).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mInfoBuilder.findViewById<TextView>(R.id.titleHeader).apply {
            text = ttl
        }
        mInfoBuilder.findViewById<TextView>(R.id.tvDesc).text = desc

        mInfoBuilder.findViewById<TextView>(R.id.tvDone)
            .setOnClickListener {
                mInfoBuilder.dismiss()
            }
        mInfoBuilder.show();
    }


    private fun callAPI(clearFilter: Boolean = false) {
        if (isInternetConnected()) {
            if (clearFilter) {
                form = ""
                dosage = ""
                quantity = ""
            }
            hashMapOf<String, String>().apply {
                put("drug_generic_name", drug_generic_name)
                put("drug_name", drug_name)
                put("is_location", is_location)
                put("from_drug_search", from_drug_search)
                put("from_drug_search", from_drug_search)
                put("sort_type", sort_type)
                put("language_code", mPrefs.getKeyValue(PreferenceConstants.LANGUAGE))
                if (form.isNotEmpty()) put("form", form)
                if (dosage.isNotEmpty()) put("dosage", dosage)
                if (quantity.isNotEmpty()) put("quantity", quantity)
                if (is_location.equals("true")) {
                    if (latitude.isNotEmpty()) put(
                        "latitude", latitude
                    )
                    if (longitude.isNotEmpty()) put(
                        "longitude", longitude
                    )
                }
                if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
                    put("membership_type", mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE))
                presenter.pharmacyList(this)
            }
        }
    }

    override fun onAddedFavResp(it: SuccessMessageEntity) {
        ivFav.isChecked = true
        pharmacyData!!.isFavorite = "true"
        pharmacyData!!.favoriteMedicationId = it.data.favorite_medication_id
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token) {  }
        } catch (e: Exception) {
        }
        Intent("UPDATE_FAVS").apply {
            putExtra("updated", true)
            LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(this)
        }
    }

    override fun onDeleteFavoriteMedicationResp(it: FavoriteMedicationEntity) {
        ivFav.isChecked = false
        pharmacyData!!.isFavorite = "false"
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token) {  }
        } catch (e: Exception) {
        }
        Intent("UPDATE_FAVS").apply {
            putExtra("updated", true)
            LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(this)
        }
    }

    override fun onPharmacyResponse(
        it: PharmacyListData,
        auth: Auth
    ) {
        pharmacyData = it
        mPharmacyList.clear()
        mdrugInfo.clear()
        mdrugImages.clear()
        mPharmacyBrands.clear()
        mPharmacyDosages.clear()
        mPharmacyForms.clear()
        mPharmacyQuantities.clear()

        mClDruginfo.visibility=View.GONE
        mTvDrugInfo.isChecked=false
        filterLayout.visibility=View.VISIBLE
        tvdiscountNear.visibility=View.VISIBLE
        tvBestDiscount.visibility=View.VISIBLE


        it.brands.forEachIndexed { index, pharmacyBrand ->
            mPharmacyBrands.add(
                PharmacyFilterEntity(
                    pharmacyBrand.label,
                    pharmacyBrand.value,
                    pharmacyBrand.value == it.drug_name
                    , "brand"
                )
            )
        }
        it.dosages.forEachIndexed { index, s ->
            mPharmacyDosages.add(
                PharmacyFilterEntity(
                    s ?: "",
                    s ?: "",
                    s == it.dosage
                    , "dosage"
                )
            )
        }

        if (it.dosage.isEmpty() && it.quantity.isEmpty()) {
            tvDosageQuantity.text = ""
        } else {
            tvDosageQuantity.text = it.dosage.toString() + " " + it.quantity
        }
        it.forms.forEachIndexed { index, s ->
            mPharmacyForms.add(PharmacyFilterEntity(s ?: "", s ?: "", s == it.form, "forms"))
        }
        var matched = false
        it.quantities.forEachIndexed { index, s ->
            if (s == quantity) {
                matched = true
            }
            mPharmacyQuantities.add(PharmacyFilterEntity(s ?: "", s ?: "", s == it.quantity, "qty"))
        }
        cutomeQty = if (matched.not()) (quantity) else ""
        ivFav.isChecked = it.isFavorite == "true"
        ivPharmacyLogo.loadImageRadius(it.image_url)
        tvPharmacyName.text =
            if (it.isDrugGeneric.equals("true")) it.drug_generic_final_name else it.drug_brand_final_name
        tvPharmacyCate.text =
            if (it.isDrugGeneric.equals("true")) it.drug_brand_final_name else it.drug_generic_final_name
        tvBestDiscount.text =
            getString(com.app.givebackrx.R.string.best_price_, it.best_discount ?: "0.00")

        mPharmacyList.addAll(it.pharmacies)
        mdrugInfo.addAll(it.drug_info)
        mdrugImages.addAll(it.drug_images)

        if(mPharmacyList.isEmpty()){
            if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)){

                tvMessage.text="Discounted price information for this drug is unavailable at this time."
                tvMessage.isClickable=false

                }else
                {
                    tvMessage.isClickable=true
                    tvMessage.text="Discounted price information for this drug is unavailable at this time. However, givebackRx card members can take advantage of prescription savings anytime without the use of a coupon simply by presenting their free givebackRx silver card to the pharmacist. Click here to sign up for a free givebackRx card to start saving and giving back!"
                    tvMessage.setOnClickListener { startActivity(Intent(activity!!, PreSignUpCheckActivity::class.java)) }
                }
            tvMessage.visibility=View.VISIBLE
            rvPharmacy.visibility=View.GONE
        }else{
            tvMessage.visibility=View.GONE
            rvPharmacy.visibility=View.VISIBLE
        }
        mPharmacyAdapter.notifyDataSetChanged()
        mPharmacyImagesAdapter.notifyDataSetChanged()
        mTvCoupon.isChecked=true


        if (it.drugDescription.isNotEmpty()){
            mTvDescription.text= Html.fromHtml(it.drugDescription.toString())
        }

        if (mdrugInfo[0].drug_administration.isNotEmpty()){
            cvAdmin.visibility=View.VISIBLE
            mTvAdmin.text=Html.fromHtml(mdrugInfo[0].drug_administration.toString())
        }else{
            cvAdmin.visibility=View.GONE
        }

        if (mdrugInfo[0].drug_missed.isNotEmpty()){
            cvMissed.visibility=View.VISIBLE
            mTvMissed.text=Html.fromHtml(mdrugInfo[0].drug_missed.toString())
        }else{
            cvMissed.visibility=View.GONE
        }


        if (mdrugInfo[0].drug_class.isNotEmpty()){
            mTvDrugClass.visibility=View.VISIBLE
            mTvClass.visibility=View.VISIBLE
            mTvDrugClass.text=Html.fromHtml(mdrugInfo[0].drug_class.toString())
        }else{
            mTvClass.visibility=View.GONE
            mTvDrugClass.visibility=View.GONE
        }

        if (mdrugInfo[0].drug_availability.isNotEmpty()){
            mTvAvailability.visibility=View.VISIBLE
            mTvAvail.visibility=View.VISIBLE
            mTvAvailability.text=Html.fromHtml(mdrugInfo[0].drug_availability.toString())
        }else{
            mTvAvailability.visibility=View.GONE
            mTvAvail.visibility=View.GONE
        }

        if (mdrugInfo[0].drug_physical_description.isNotEmpty()){
            mTvPhysical.visibility=View.VISIBLE
            mTvPhy.visibility=View.VISIBLE
            mTvPhysical.text=Html.fromHtml(mdrugInfo[0].drug_physical_description.toString())
        }else{
            mTvPhysical.visibility=View.GONE
            mTvPhy.visibility=View.GONE
        }

        if (mdrugInfo[0].drug_storage.isNotEmpty()){
            mTvStor.visibility=View.VISIBLE
            mTvStorage.visibility=View.VISIBLE
            mTvStorage.text=Html.fromHtml(mdrugInfo[0].drug_storage.toString())
        }else{
            mTvStor.visibility=View.GONE
            mTvStorage.visibility=View.GONE
        }


        if (mdrugInfo[0].drug_side_effect.isNotEmpty()){
            cvSideEffects.visibility=View.VISIBLE
            mTvSideEffect.text=Html.fromHtml(mdrugInfo[0].drug_side_effect.toString())
        }else{
            cvSideEffects.visibility=View.GONE
        }

        if (mdrugInfo[0].drug_contra.isNotEmpty()){
            cvContra.visibility=View.VISIBLE
            mTvContradiction.text=Html.fromHtml(mdrugInfo[0].drug_contra.toString())
        }else{
            cvContra.visibility=View.GONE
        }
         if (mdrugInfo[0].drug_monitor.isNotEmpty()){
             cvMonitor.visibility=View.VISIBLE
             mTvMonitor.text=Html.fromHtml(mdrugInfo[0].drug_monitor.toString())
        }else{
             cvMonitor.visibility=View.GONE
        }

        if (mdrugInfo[0].drug_interaction.isNotEmpty()){
             cvMediIntrect.visibility=View.VISIBLE
             mTvMedication.text=Html.fromHtml(mdrugInfo[0].drug_interaction.toString())
        }else{
             cvMediIntrect.visibility=View.GONE
        }

        cvDescription.visibility=View.VISIBLE
        cvAdmin.visibility=View.VISIBLE
        cvClass.visibility=View.VISIBLE
        cvMissed.visibility=View.VISIBLE

        cvSideEffects.visibility=View.GONE
        cvContra.visibility=View.GONE
        cvMonitor.visibility=View.GONE
        cvMediIntrect.visibility=View.GONE
        cvImages.visibility=View.GONE
//        if (permissionFile.checklocationPermissions(requireActivity())) {
//            startLocationUpdates(null)
//        }
//        from_drug_search = "false"
        hideAllTypeKB(requireView().rootView.windowToken)
        if (rvPharmacy.visibility == View.GONE) {
            if (mMap != null) mMap!!.clear()
            mMapFragment!!.getMapAsync(this@PharmacyFragment)
        }

        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(auth.new_jwt_token) {  }
        } catch (e: Exception) {
        }

        if(GBRxApp.mSavePharmacyFavData != null){
            ivFav.performClick()
            GBRxApp.mSavePharmacyFavData=null
        }
    }


    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

    override fun onFirstListClick(item: Any, position: Int) {
        if (item is String)
            when (item) {
                "brand" -> {
                    filterItemClick(0)
                }
                "dosage" -> {
                    filterItemClick(2)
                }
                "forms" -> {
                    filterItemClick(1)
                }
                "qty" -> {
                    filterItemClick(3)
                }
            }
    }

    fun filterItemClick(item: Int) {
//        form = ""
//        dosage = ""
//        quantity = ""
//        var selectedBrand = ""
//
//        mPharmacyBrands.forEach {
//            if (it.selected) {
//                selectedBrand = it.value
//            }
//        }
//        if (item > 0)
//            mPharmacyForms.forEach { if (it.selected) form = it.value }
//        if (item > 1)
//            mPharmacyDosages.forEach { if (it.selected) dosage = it.value }
//        if (item > 2) {
//            mPharmacyQuantities.forEach { if (it.selected) quantity = it.value }
//            if (quantity.isEmpty()) {
//                quantity = "${edtQty?.text.toString()} ${pharmacyData!!.custQtyType}"
//            }
//        }
//        if (quantity.isNotEmpty()) {
//            if (dosage.isEmpty()) dosage = pharmacyData?.dosage!!
//            if (form.isEmpty()) form = pharmacyData?.form!!
//        } else if (dosage.isNotEmpty()) {
//            if (form.isEmpty()) form = pharmacyData?.form!!
//        }
//        drug_name =
//            if (selectedBrand.isEmpty()) pharmacyData?.drug_name!! else selectedBrand
//        from_drug_search = "false"
//        if (bSheet != null) bSheet!!.dismiss()
//        callAPI()

        form = ""
        dosage = ""
        quantity = ""
        var selectedBrand = ""

        mPharmacyBrands.forEach {
            if (it.selected) {
                selectedBrand = it.value
            }
        }
        if (item > 0)
            mPharmacyForms.forEach { if (it.selected) form = it.value }
        if (item > 1)
            mPharmacyDosages.forEach { if (it.selected) dosage = it.value }
        if (item > 2) {
            if (edtQty?.text.toString().isNotEmpty()&&pharmacyData?.isCustQtyAllowed.equals("true"))
                quantity = "${edtQty?.text.toString()} ${pharmacyData!!.custQtyType}"
            else
                mPharmacyQuantities.forEach { if (it.selected) quantity = it.value }
            if (quantity.isEmpty()) {
                quantity = "${edtQty?.text.toString()} ${pharmacyData!!.custQtyType}"
            }
        }
        if (quantity.isNotEmpty()) {
            if (dosage.isEmpty()) dosage = pharmacyData?.dosage!!
            if (form.isEmpty()) form = pharmacyData?.form!!
        } else if (dosage.isNotEmpty()) {
            if (form.isEmpty()) form = pharmacyData?.form!!
        }
        drug_name =
            if (selectedBrand.isEmpty()) pharmacyData?.drug_name!! else selectedBrand
        from_drug_search = "false"
        if (bSheet != null) bSheet!!.dismiss()
        callAPI()
    }

    override fun onSecondListClick(item: Any, position: Int) {

    }


    override fun onSingleListClick(item: Any, position: Int) {
        if (item is String) {
            if (item == "gold_update") {
                upgradeCard {
                    val args = Bundle()
                    args.putString("from_dashboard", "no")
                    val paymentFragment = PaymentFragment()
                    paymentFragment.setArguments(args)
                    mainActivity.addMainFragment(R.id.homeContainer, paymentFragment)
                }
            } else if (item == "zoom_map") {
                selectedPositionForMap = position
                tvMapView.performClick()
            } else {
                DrugDetailFragment.pricebook_entry_id = mPharmacyList[position].pricebook_entry_id
                DrugDetailFragment.pharmacy_address_id = mPharmacyList[position].pharmacy_address_id
                mainActivity.addMainFragment(R.id.homeContainer, DrugDetailFragment())
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GlobalVariable.REQUEST_CODE_LOCATION && permissions.size > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) CommonUtil.permissionAlert(
                requireActivity(),
                getString(R.string.location_permission_dialog)
            )
            else {
                if (mPrefs.getKeyValue(PreferenceConstants.LAT).isNotEmpty()) {
                    is_location = "true"
                    if (latitude.isEmpty())
                        latitude = mPrefs.getKeyValue(PreferenceConstants.LAT)
                    if (longitude.isEmpty())
                        longitude = mPrefs.getKeyValue(PreferenceConstants.LNG)
                    callAPI()
                } else {
                    startLocationUpdates {
                        is_location = "true"
                        if (latitude.isEmpty())
                            latitude = mPrefs.getKeyValue(PreferenceConstants.LAT)
                        if (longitude.isEmpty())
                            longitude = mPrefs.getKeyValue(PreferenceConstants.LNG)
                        callAPI()
                    }
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            ivBack -> {
                requireActivity().onBackPressed()
            }
            ivInfo -> {
                if (pharmacyData != null && pharmacyData!!.drugDescription.isNotEmpty())
                    showInformationPop("Drug Description", pharmacyData!!.drugDescription)
                else toast("Drug information not available.", false)
            }
            ivFav -> {
                if (!mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
                    AllinOneDialog(ttle = "Add Favorite",
                        msg = "Please sign in to save a medication to your favorites.",
                        onLeftClick = {/*btn No click*/ },
                        onRightClick = {/*btn Yes click*/
                            GBRxApp.mSavePharmacyFavData = SavePharmacyFavData(
                                drug_generic_name,
                                drug_name,
                                is_location,
                                from_drug_search,
                                form,
                                dosage,
                                quantity,
                                sort_type,
                                latitude,
                                longitude
                            )
                            startActivity(
                                Intent(
                                    requireActivity(),
                                    LoginEmailPasswordActivity::class.java
                                )
                            )
                        }
                    )
                } else {
                    if (isInternetConnected())
                        if (ivFav.isChecked.not()) {
                            JsonObject().apply {
                                addProperty(
                                    "user_type",
                                    mPrefs.getKeyValue(PreferenceConstants.USER_TYPE)
                                )
                                addProperty("drug_name", pharmacyData!!.drug_name)
                                addProperty(
                                    "generic_name",
                                    pharmacyData!!.drug_generic_name
                                )
                                addProperty("form", pharmacyData!!.form)
                                addProperty("dosage", pharmacyData!!.dosage)
                                addProperty("quantity", pharmacyData!!.quantity)
                                addProperty("drug_ndc", pharmacyData!!.drugNdc)
                                addProperty(
                                    "is_cust_qty_allowed",
                                    pharmacyData!!.isCustQtyAllowed
                                )
                                addProperty(
                                    "cust_qty_type",
                                    pharmacyData!!.custQtyType
                                )
                                addProperty(
                                    "internal_drug_pack_size",
                                    pharmacyData!!.internalDrugPackSize
                                )
                                addProperty(
                                    "is_drug_generic",
                                    pharmacyData!!.isDrugGeneric
                                )
                                addProperty("is_location", is_location)
                                if (PharmacyFragment.latitude.isNotEmpty())
                                    addProperty("latitude", latitude)
                                if (PharmacyFragment.longitude.isNotEmpty())
                                    addProperty("longitude", longitude)
                                presenter.addFaveMed(this)
                            }
                        } else {
                            if (isInternetConnected())
                                presenter.deleteFavoriteMedication(pharmacyData!!.favoriteMedicationId)
                        }
                }
                GBRxApp.mSavePharmacyFavData=null
            }
            tvSearch -> {
                openPlaceDialog()
            }
            tvMapView -> {
                if (rvPharmacy.visibility == View.GONE) {
                    tvMapView.text = "Map View"
                    if (mMap != null) {
                        mMap!!.clear()
                        mMap!!.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(0.0, 0.0),
                                8f
                            ), 100, null
                        )
                    }
                    ConstMapView.onOffVisibility(false)
                    rvPharmacy.onOffVisibility(true)
                    mClDruginfo.visibility=View.GONE
                } else {
                    if (pharmacyData != null) {
//                        if (permissionFile.checklocationPermissions(requireActivity())) {
                        tvMapView.text = "List View"
                        mTvCoupon.isChecked=true
                        mTvDrugInfo.isChecked=false
                        ConstMapView.onOffVisibility(true)
                        mClDruginfo.visibility=View.GONE
                        rvPharmacy.onOffVisibility(false)
                        if (mMap != null) mMap!!.clear()
                        mMapFragment!!.getMapAsync(this@PharmacyFragment)
//                        }
                    }
                }
            }
            tvSortPrice -> {
                dosage = pharmacyData!!.dosage
                form = pharmacyData!!.form
                quantity = pharmacyData!!.quantity
                sort_type = "Price"
                if (mPrefs.getKeyValue(PreferenceConstants.LAT).isNotEmpty()) {
                    is_location = "true"
                    if (latitude.isEmpty())
                        latitude = mPrefs.getKeyValue(PreferenceConstants.LAT)
                    if (longitude.isEmpty())
                        longitude = mPrefs.getKeyValue(PreferenceConstants.LNG)
                    callAPI(false)
                    tvSortPrice.isChecked = true
                    tvSortDistance.isChecked = false
                    tvFilter.isChecked = false
                } else {
                    is_location = "false"
                    callAPI(false)
                    tvSortPrice.isChecked = true
                    tvSortDistance.isChecked = false
                    tvFilter.isChecked = false
                }
//                if (permissionFile.checklocationPermissions(requireActivity())) {
//                    if (mPrefs.getKeyValue(PreferenceConstants.LAT).isNotEmpty()) {
//                        is_location = "true"
//                        from_drug_search = "false"
//                        sort_type = "Price"
//                        callAPI(false)
//                    } else {
//                        startLocationUpdates {
//                            latitude = mPrefs.getKeyValue(PreferenceConstants.LAT)
//                            longitude = mPrefs.getKeyValue(PreferenceConstants.LNG)
//                            is_location = "true"
//                            sort_type = "Price"
//                            from_drug_search = "false"
//                            callAPI(false)
//                        }
//                    }
//                } else {
//                    sort_type = ""
//                    is_location = "false"
//                    from_drug_search = "false"
//                    callAPI(false)
//                }
                tvSortPrice.isChecked = true
                tvSortDistance.isChecked = false
                tvFilter.isChecked = false
            }
            tvSortDistance -> {
                dosage = pharmacyData!!.dosage
                form = pharmacyData!!.form
                quantity = pharmacyData!!.quantity
//                from_drug_search = "false"
                if (permissionFile.checklocationPermissions(requireActivity())) {
                    if (mPrefs.getKeyValue(PreferenceConstants.LAT).isNotEmpty()) {
                        is_location = "true"
                        sort_type = "Distance"
                        latitude = mPrefs.getKeyValue(PreferenceConstants.LAT)
                        longitude = mPrefs.getKeyValue(PreferenceConstants.LNG)
                        callAPI(false)
                    } else {
                        startLocationUpdates {
                            sort_type = "Distance"
                            is_location = "true"
                            latitude = mPrefs.getKeyValue(PreferenceConstants.LAT)
                            longitude = mPrefs.getKeyValue(PreferenceConstants.LNG)
                            callAPI(false)
                        }
                    }
                }
                tvSortPrice.isChecked = false
                tvSortDistance.isChecked = true
                tvFilter.isChecked = false
            }
            mTvCoupon->{
                mTvCoupon.isChecked=true
                mTvDrugInfo.isChecked=false
                filterLayout.visibility=View.VISIBLE
                tvdiscountNear.visibility=View.VISIBLE
                tvBestDiscount.visibility=View.VISIBLE
                rvPharmacy.visibility=View.VISIBLE
                mClDruginfo.visibility=View.GONE
            }
            mTvDrugInfo->{
                mTvCoupon.isChecked=false
                mTvDrugInfo.isChecked=true
                filterLayout.visibility=View.GONE
                rvPharmacy.visibility=View.GONE
                tvdiscountNear.visibility=View.GONE
                tvBestDiscount.visibility=View.GONE
                mClDruginfo.visibility=View.VISIBLE

                mTvBasics.isChecked=true
            }

            mTvBasics->{

                mTvBasics.isChecked=true
                mTvSideEffects.isChecked=false
                mTvImages.isChecked=false

                cvDescription.visibility=View.VISIBLE
                cvAdmin.visibility=View.VISIBLE
                cvClass.visibility=View.VISIBLE
                cvMissed.visibility=View.VISIBLE

                cvSideEffects.visibility=View.GONE
                cvContra.visibility=View.GONE
                cvMonitor.visibility=View.GONE
                cvMediIntrect.visibility=View.GONE
                cvImages.visibility=View.GONE
            }
            mTvSideEffects->{
                mTvBasics.isChecked=false
                mTvSideEffects.isChecked=true
                mTvImages.isChecked=false

                cvDescription.visibility=View.GONE
                cvAdmin.visibility=View.GONE
                cvClass.visibility=View.GONE
                cvMissed.visibility=View.GONE
                cvImages.visibility=View.GONE

                cvSideEffects.visibility=View.VISIBLE
                cvContra.visibility=View.VISIBLE
                cvMonitor.visibility=View.VISIBLE
                cvMediIntrect.visibility=View.VISIBLE
            }
            mTvImages->{
                mTvBasics.isChecked=false
                mTvSideEffects.isChecked=false
                mTvImages.isChecked=true

                cvImages.visibility=View.VISIBLE

                cvDescription.visibility=View.GONE
                cvAdmin.visibility=View.GONE
                cvClass.visibility=View.GONE
                cvMissed.visibility=View.GONE

                cvSideEffects.visibility=View.GONE
                cvContra.visibility=View.GONE
                cvMonitor.visibility=View.GONE
                cvMediIntrect.visibility=View.GONE
            }
            tvFilter -> {
//                tvSortPrice.isChecked = false
//                tvSortDistance.isChecked = false
//                tvFilter.isChecked = true
                bottomSheetDialog(R.layout.item_filter_bottomsheet) { sheetView, sheet ->
                    bSheet = sheet
//                    if (form.isEmpty() && dosage.isEmpty() && quantity.isEmpty())
//                        resetFilterListCheck()

                    sheetView.findViewById<RecyclerView>(R.id.rvValues).adapter = mFilterAdapter
                    edtQty = sheetView.findViewById<EditText>(R.id.edtQty)
                    btnQty = sheetView.findViewById<TextView>(R.id.btnQty)
                    sheetView.findViewById<CheckedTextView>(R.id.tvBrands)
                        .setOnSingleClickListener {
                            edtQty?.onOffVisibility(false)
                            btnQty?.onOffVisibility(false)
                            if ((it as CheckedTextView).isChecked.not()) changeFilterData(
                                it.id,
                                sheetView,
                                0
                            )
                        }
                    sheetView.findViewById<CheckedTextView>(R.id.tvForms).setOnSingleClickListener {
                        edtQty?.onOffVisibility(false)
                        btnQty?.onOffVisibility(false)
                        if ((it as CheckedTextView).isChecked.not()) changeFilterData(
                            it.id,
                            sheetView,
                            1
                        )
                    }
                    sheetView.findViewById<CheckedTextView>(R.id.tvDose).setOnSingleClickListener {
                        edtQty?.onOffVisibility(false)
                        btnQty?.onOffVisibility(false)
                        if ((it as CheckedTextView).isChecked.not()) changeFilterData(
                            it.id,
                            sheetView,
                            2
                        )
                    }
                    sheetView.findViewById<CheckedTextView>(R.id.tvQuantity)
                        .setOnSingleClickListener {
                            edtQty?.onOffVisibility(pharmacyData?.isCustQtyAllowed.equals("true"))
                            edtQty?.setText(cutomeQty)
                            btnQty?.onOffVisibility(pharmacyData?.isCustQtyAllowed.equals("true"))
                            if ((it as CheckedTextView).isChecked.not()) changeFilterData(
                                it.id,
                                sheetView,
                                3
                            )
                        }

                    btnQty?.setOnSingleClickListener {
                        filterItemClick(3)
                    }
                    sheetView.findViewById<TextView>(R.id.tvClose)
                        .setOnSingleClickListener { sheet.dismiss() }
                    sheetView.findViewById<TextView>(R.id.tvCancel)
                        .setOnSingleClickListener {
                            sheet.dismiss()
//                            sort_type=""
//                            from_drug_search = "false"
//                            callAPI(true)
                        }
                    sheetView.findViewById<TextView>(R.id.tvApply).setOnSingleClickListener {
                        form = ""
                        dosage = ""
                        quantity = ""
                        var selectedBrand = ""
                        mPharmacyBrands.forEach {
                            if (it.selected) {
                                selectedBrand = it.value
                            }
                        }
                        mPharmacyForms.forEach { if (it.selected) form = it.value }
                        mPharmacyDosages.forEach { if (it.selected) dosage = it.value }
                        mPharmacyQuantities.forEach { if (it.selected) quantity = it.value }

                        if (quantity.isNotEmpty()) {
                            if (dosage.isEmpty()) dosage = pharmacyData?.dosage!!
                            if (form.isEmpty()) form = pharmacyData?.form!!
                            drug_name =
                                if (selectedBrand.isEmpty()) pharmacyData?.drug_name!! else selectedBrand
                        } else if (dosage.isNotEmpty()) {
                            if (form.isEmpty()) form = pharmacyData?.form!!
                            drug_name =
                                if (selectedBrand.isEmpty()) pharmacyData?.drug_name!! else selectedBrand
                        } else if (form.isNotEmpty()) {
                            drug_name =
                                if (selectedBrand.isEmpty()) pharmacyData?.drug_name!! else selectedBrand
                        }

                        sheet.dismiss()
                        from_drug_search = "false"
                        callAPI()
                    }
                    changeFilterData(
                        sheetView.findViewById<CheckedTextView>(R.id.tvBrands).id,
                        sheetView,
                        0
                    )//default selected
                }
            }
        }
    }


    fun openPlaceDialog() {
        val fields = Arrays.asList(
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS_COMPONENTS
        )
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(requireActivity())
        startActivityForResult(intent, AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            hideKeyboardDialog(requireActivity().currentFocus)
            if (requestCode == AppConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                try {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    if (place.latLng != null) {
                        from_drug_search = "false"
                        is_location = "true"
                        sort_type = "Distance"
                        latitude = place.latLng!!.latitude.toString()
                        longitude = place.latLng!!.longitude.toString()
                        mPrefs.setKeyValue(PreferenceConstants.LAT, latitude)
                        mPrefs.setKeyValue(PreferenceConstants.LNG, longitude)
//                        tvSearch.text=place.address.toString()
                        callAPI()
                    }
                } catch (e: java.lang.Exception) {
                    toast("Something went wrong, Please Try later!", false)
                }
            }
        }
    }

    ///////////////////////Filter/////////////////////
    private fun changeFilterData(viewId: Int, sheetView: View, listId: Int) {

        sheetView.findViewById<CheckedTextView>(R.id.tvBrands).apply {
            isChecked = this.id == viewId
        }
        sheetView.findViewById<CheckedTextView>(R.id.tvForms).apply {
            isChecked = this.id == viewId
        }
        sheetView.findViewById<CheckedTextView>(R.id.tvDose).apply {
            isChecked = this.id == viewId
        }
        sheetView.findViewById<CheckedTextView>(R.id.tvQuantity).apply {
            isChecked = this.id == viewId
        }

        when (listId) {
            0 -> {
                mFilterAdapter.replaceList(mPharmacyBrands)
            }
            1 -> {
                mFilterAdapter.replaceList(mPharmacyForms)
            }
            2 -> {
                mFilterAdapter.replaceList(mPharmacyDosages)
            }
            3 -> {
                mFilterAdapter.replaceList(mPharmacyQuantities)
            }
        }
        mFilterAdapter.notifyDataSetChanged()
    }

    private fun resetFilterListCheck() {
//        mPharmacyBrands.forEachIndexed { index, pharmacyFilterEntity ->
//            pharmacyFilterEntity.selected = pharmacyFilterEntity.value == pharmacyData?.drug_name
//        }
//        mPharmacyDosages.forEachIndexed { index, pharmacyFilterEntity ->
//            pharmacyFilterEntity.selected = pharmacyFilterEntity.value == pharmacyData?.dosage
//        }
//        mPharmacyForms.forEachIndexed { index, pharmacyFilterEntity ->
//            pharmacyFilterEntity.selected = pharmacyFilterEntity.value == pharmacyData?.form
//        }
//        mPharmacyQuantities.forEachIndexed { index, pharmacyFilterEntity ->
//            pharmacyFilterEntity.selected = pharmacyFilterEntity.value == pharmacyData?.quantity
//        }
    }

    override fun onResume() {
        super.onResume()
        changeTheme(R.style.AppBlueTheme)
        MainActivity.canClose = false
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
            broardReceiver,
            IntentFilter("UPDATE_FAVS")
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            val markersList = ArrayList<Marker>()
            mMap = googleMap
            mMap!!.uiSettings.isCompassEnabled = false
            mMap!!.uiSettings.isMapToolbarEnabled = false
            mMap!!.uiSettings.isMyLocationButtonEnabled = true
//            if (ActivityCompat.checkSelfPermission(
//                    requireActivity(),
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    requireActivity(),
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                return
//            }

            mMap!!.clear()
            var selectedMarker: Marker? = null
            var markerOptions: MarkerOptions? = null
            pharmacyData!!.pharmacies.forEachIndexed { index, pharmacyData ->
                if (pharmacyData.latitude.isNullOrEmpty()
                        .not() && pharmacyData.longitude.isNullOrEmpty().not()
                ) {
                    var amount = ""
                    if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
                        amount = if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
                                .toLowerCase(Locale.ENGLISH).equals("silver")
                        )
                            pharmacyData.silver_price else pharmacyData.gold_price
                    } else {
                        amount = pharmacyData.marketplace_price
                    }
                    markerOptions = MarkerOptions().draggable(false)
                        .title(pharmacyData.pharmacy_name + ":+:" + amount)
                        .position(
                            LatLng(
                                pharmacyData.latitude.toDouble(),
                                pharmacyData.longitude.toDouble()
                            )
                        )
                        .snippet(index.toString())
                    val marker = mMap!!.addMarker(markerOptions)
                    if (index == selectedPositionForMap) selectedMarker = marker
                    markersList.add(marker)
                    mMap!!.setInfoWindowAdapter(this)
                    mMap!!.setOnInfoWindowClickListener(this)
                }
            }

            if (selectedPositionForMap == -1) {
//                if (markersList.size == 1) {
//                    mMap!!.animateCamera(
//                        CameraUpdateFactory.newLatLngZoom(
//                            markersList[0].position,
//                            13f
//                        ), 500, null
//                    )
//                } else {
//                    if (markersList.size > 0) {
//                        val builder = LatLngBounds.Builder()
//                        for (j in markersList.indices) {
//                            builder.include(markersList[j].position)
//                        }
//                        val padding = 25
//                        val bounds = builder.build()
//                        mMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
//                    }
//                }

                if (markersList.isNotEmpty())
                mMap!!.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        markersList[if(markersList.size>1)markersList.size/2.toInt() else 0].position,
                        12f
                    ), 500, null
                )
                //
            } else {
                if (selectedMarker != null) {
                    mMap!!.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            selectedMarker!!.position,
                            16f
                        ), 500, null
                    )
                    selectedMarker!!.showInfoWindow()
                }
                selectedPositionForMap = -1
            }
        } catch (e: Exception) {
        }

    }

    override fun onInfoWindowClick(p0: Marker?) {
        onSingleListClick("detail", p0!!.snippet!!.toInt())
    }

    override fun getInfoContents(marker: Marker): View {
        val titleValue = marker.title.substringBefore(":+:")
        val amountValue = marker.title.substringAfter(":+:")
        val info = LinearLayout(activity)
        info.orientation = LinearLayout.VERTICAL
        info.gravity = Gravity.CENTER
        val title = TextView(activity)
        title.gravity = Gravity.CENTER
        title.setTypeface(null, Typeface.BOLD)
        title.isAllCaps = false
        title.setText(titleValue)
        title.textSize = 14f
        title.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black_color))
        val tvAmount = TextView(activity)
        tvAmount.setText("$$amountValue")
        tvAmount.textSize = 12f
        tvAmount.setTextColor(ContextCompat.getColor(requireActivity(), R.color.light_gray_color))
        val coupon = TextView(activity)
        coupon.gravity = Gravity.CENTER
        coupon.setTypeface(null, Typeface.BOLD)
        coupon.isAllCaps = true
        coupon.setText("Get this coupon")
        coupon.textSize = 13f
        coupon.setTextColor(ContextCompat.getColor(requireActivity(), R.color.red_color))
        info.addView(title);
        info.addView(tvAmount)
        info.addView(coupon)
        info.setPadding(2, 2, 2, 2)
        return info
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(broardReceiver)
    }

    val broardReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1!!.hasExtra("updated")) {
                if (p1.getBooleanExtra("updated", true)) {
                    if (pharmacyData != null)
                        ivFav.isChecked = pharmacyData!!.isFavorite == "true"
                }
            }
        }
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }

    companion object {
        var drug_generic_name = ""
        var drug_name = ""
        var is_location = "false"
        var from_drug_search = "true"
        var form = ""
        var dosage = ""
        var quantity = ""
        var sort_type = "Price"
        var latitude = ""
        var longitude = ""
        var pharmacyData: PharmacyListData? = null

    }


    private fun resetAPIValues() {
        is_location = "false"
        from_drug_search = "true"
        form = ""
        dosage = ""
        quantity = ""
        sort_type = "Price"
        latitude = ""
        longitude = ""
        pharmacyData = null
    }
}