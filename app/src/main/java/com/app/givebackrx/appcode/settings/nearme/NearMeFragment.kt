package com.app.givebackrx.appcode.settings.nearme

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.app.givebackrx.R
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.NearMeData
import com.app.givebackrx.data.entity.NearMeEntity
import com.app.givebackrx.listeners.SingleListCLickListener
import com.app.givebackrx.utils.AppConstants
import com.app.givebackrx.utils.CommonUtil
import com.app.givebackrx.utils.GlobalVariable
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.onOffVisibility
import com.app.givebackrx.utils.extension.setOnSingleClickListener
import com.app.givebackrx.utils.extension.toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.fragment_near_me.*
import java.util.*
import javax.inject.Inject


class NearMeFragment : BaseFragment(), View.OnClickListener, GoogleMap.InfoWindowAdapter,
    GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback, SingleListCLickListener, INearMeView {

    var defaultDistance = 10
    var defaultZoom = 12f
    var clearList: Boolean = false
    val mNearMeList = mutableListOf<NearMeData>()
    val mNearMeAdapter by lazy { NearMeAdapter(mNearMeList, this) }
    var selectedPositionForMap: Int = -1
    private var mMap: GoogleMap? = null
    var mMapFragment: SupportMapFragment? = null
    var rootView: View? = null

    @Inject
    lateinit var presenter: NearMePresenter<INearMeView>

    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        ivBack.setOnClickListener(this)
        tvMapView.setOnClickListener(this)
        ivMyLocation.setOnSingleClickListener(this)
        btnZipSearch.setOnSingleClickListener(this)
        tvSearch.setOnSingleClickListener(this)
        rvPharmacy.adapter = mNearMeAdapter

        getNearMePharmacy()
    }

    private fun getNearMePharmacy(lat: String? = null, lng: String? = null) {
        hideAllTypeKB(requireView().windowToken)
        if (isInternetConnected()) {
            if (zipSearch.text.isEmpty()) {
                if (permissionFile.checklocationPermissions(requireActivity())) {
                    if (mPrefs.getKeyValue(PreferenceConstants.LAT).isNotEmpty()) {
                        presenter.pharmacyNearMe(
                            if (zipSearch.text.isEmpty())
                                lat ?: mPrefs.getKeyValue(PreferenceConstants.LAT) else "",
                            if (zipSearch.text.isEmpty()) lng
                                ?: mPrefs.getKeyValue(PreferenceConstants.LNG) else "",
                            "$defaultDistance",//https://api-dev.giveback-rx.com/v1/pharmacyNearMe?latitude=29.726569&longitude=-98.094834&distance=5&zipcode=10001
                            zipSearch.text.toString()
                        )
                    } else {
                        startLocationUpdates {
                            presenter.pharmacyNearMe(
                                if (zipSearch.text.isEmpty()) lat ?: mPrefs.getKeyValue(
                                    PreferenceConstants.LAT
                                ) else "",
                                if (zipSearch.text.isEmpty()) lng ?: mPrefs.getKeyValue(
                                    PreferenceConstants.LNG
                                ) else "",
                                "$defaultDistance",
                                zipSearch.text.toString()
                            )
                        }
                    }
                }
            }else{
                presenter.pharmacyNearMe("", "", "$defaultDistance", zipSearch.text.toString())
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (rootView != null) {
            (rootView!!.parent as ViewGroup).removeView(rootView)
        }
        rootView = inflater.inflate(R.layout.fragment_near_me, container, false)
        try {
            mMapFragment =
                this.childFragmentManager.findFragmentById(R.id.fl_map) as SupportMapFragment
            MapsInitializer.initialize(requireActivity())
        } catch (e: Exception) {
        }
        return rootView
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
            ) { requireActivity().onBackPressed() }
            else {
                getNearMePharmacy()
            }
        }
    }

    override fun onNearMeResp(it: NearMeEntity) {
        if (clearList)
            mNearMeList.clear()
        mNearMeList.addAll(it.data)
        mNearMeAdapter.notifyDataSetChanged()
        clearList = false
        if (mNearMeList.isNotEmpty() && tvMapView.text == "List View")
            if (permissionFile.checklocationPermissions(requireActivity())) {
                mMapFragment!!.getMapAsync(this@NearMeFragment)
            }
        if (rvPharmacy.visibility == View.GONE){
            if (mMap!=null) mMap!!.clear()
            mMapFragment!!.getMapAsync(this@NearMeFragment)
        }
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token) {  }
        } catch (e: Exception) {
        }
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

    override fun onClick(p0: View?) {
        when (p0) {
            tvSearch -> openPlaceDialog()
            ivBack -> requireActivity().onBackPressed()
            ivMyLocation -> {
                clearList = true
                getNearMePharmacy()
            }
            btnZipSearch -> {
                clearList=true
                if (zipSearch.text.length < 5) {
                    hideAllTypeKB(requireView().rootView.windowToken)
                    toast(getString(R.string.valid_zip), false)
                } else
                    getNearMePharmacy()
            }
            tvMapView -> {
                if (rvPharmacy.visibility == View.GONE) {
                    tvMapView.text = getString(R.string.mapview)
                    ConstMapView.onOffVisibility(false)
                    rvPharmacy.onOffVisibility(true)
                    if(mMap!=null) {
                        mMap!!.clear()
                        mMap!!.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(0.0,0.0),
                                8f
                            ), 100, null
                        )
                    }
                } else {
                    defaultDistance = 10
                    defaultZoom = 12f
                    if (mNearMeList.isNotEmpty())
//                        if (permissionFile.checklocationPermissions(requireActivity())) {
                        tvMapView.text = getString(R.string.listview)
                    ConstMapView.onOffVisibility(true)
                    rvPharmacy.onOffVisibility(false)
                    mMapFragment!!.getMapAsync(this@NearMeFragment)
//                        }
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
                        getNearMePharmacy(
                            place.latLng!!.latitude.toString(),
                            place.latLng!!.latitude.toString()
                        )
                    }
                } catch (e: java.lang.Exception) {
                    toast("Something went wrong, Please Try later!", false)
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            val markersList = ArrayList<Marker>()
            mMap = googleMap
            mMap!!.uiSettings.isCompassEnabled = false
            mMap!!.uiSettings.isZoomControlsEnabled = true
            mMap!!.setOnCameraIdleListener {
                val zoomLevel: Float = mMap!!.getCameraPosition().zoom
                if (12 - zoomLevel > 0 && defaultZoom != zoomLevel) {
                    defaultZoom = zoomLevel
                    var currentZoom = 10 + ((12 - zoomLevel) * 10).toInt()
                    if (defaultDistance != currentZoom)
                        defaultDistance = currentZoom
                    getNearMePharmacy()
                    clearList = true
                }
            }
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
            var selectedMarker: Marker? = null
            var markerOptions: MarkerOptions? = null
            mNearMeList.forEachIndexed { index, data ->
                if (data.Latitude.isNullOrEmpty()
                        .not() && data.Longitude.isNullOrEmpty().not()
                ) {
                    markerOptions = MarkerOptions().draggable(false)
                        .title("${data.Name}*#*${data.Address}")
                        .position(
                            LatLng(
                                data.Latitude.toDouble(),
                                data.Longitude.toDouble()
                            )
                        )
                        .snippet(index.toString())
                    val marker = mMap!!.addMarker(markerOptions)
                    markersList.add(marker)
                    mMap!!.setInfoWindowAdapter(this)
                    mMap!!.setOnInfoWindowClickListener(this)
                    if (index == selectedPositionForMap) selectedMarker = marker
                    if (selectedPositionForMap == -1) {
                        mMap!!.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                marker.position,
                                defaultZoom.toFloat()
                            ), 500, null
                        )
                    }
                }
            }

            //animate to selected position
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


//            if (markersList.size == 1) {
//                mMap!!.animateCamera(
//                    CameraUpdateFactory.newLatLngZoom(
//                        markersList[0].position,
//                        12f
//                    ), 500, null
//                )
//            }
//            } else {
//                if (markersList.size > 0) {
//                    val builder = LatLngBounds.Builder()
//                    for (j in markersList.indices) {
//                        builder.include(markersList[j].position)
//                    }
//                    val padding = 25
//                    val bounds = builder.build()
//                    mMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
//                }
//            }
        } catch (e: Exception) {
        }

    }

    override fun onInfoWindowClick(p0: Marker?) {
//        onSingleListClick(mPharmacyList[p0!!.snippet!!.toInt()], p0.snippet!!.toInt())
    }

    override fun getInfoContents(marker: Marker): View {
        val info = LinearLayout(activity)
        info.orientation = LinearLayout.VERTICAL
        info.gravity = Gravity.CENTER
        val title = TextView(activity)
        title.gravity = Gravity.CENTER
        title.setTypeface(null, Typeface.BOLD)
        title.isAllCaps = false
        title.text = marker.title.substringBefore("*#*")
        title.textSize = 14f
        title.setTextColor(ContextCompat.getColor(requireActivity(), R.color.green_color))
        val desc = TextView(activity)
        desc.gravity = Gravity.CENTER
        desc.setTypeface(null, Typeface.BOLD)
        desc.isAllCaps = false
        desc.text = marker.title.substringAfter("*#*")
        desc.textSize = 13f
        desc.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black_color))

        info.addView(title);
        info.addView(desc);
        info.setPadding(3, 3, 3, 3)
        return info
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }

    override fun onSingleListClick(item: Any, position: Int) {
        selectedPositionForMap = position
        tvMapView.performClick()
    }

}