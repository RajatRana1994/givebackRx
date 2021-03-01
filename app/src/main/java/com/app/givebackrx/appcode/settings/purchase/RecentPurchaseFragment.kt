package com.app.givebackrx.appcode.settings.purchase

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.GBRxApp
import com.app.givebackrx.R
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.RecentPurchase
import com.app.givebackrx.data.entity.RecentPurchaseEntity
import com.app.givebackrx.listeners.SingleListCLickListener
import com.app.givebackrx.utils.PreferenceConstants
import kotlinx.android.synthetic.main.fragment_recent_purchase.*
import javax.inject.Inject

class RecentPurchaseFragment : BaseFragment(), IRecentPurchaseView,
    AdapterView.OnItemSelectedListener, SingleListCLickListener {

    @Inject
    lateinit var presenter: RecentPurchasePresenter<IRecentPurchaseView>
    var count = 0
    var timeStr = "Time"
    var medStr = "Medication"
    var pharStr = "Pharmacy"
    lateinit var mTimeAdapter: ArrayAdapter<String>
    lateinit var mMedicationAdapter: ArrayAdapter<String>
    lateinit var mPharmacyAdapter: ArrayAdapter<String>
    val mRecentPurchaseList by lazy { mutableListOf<RecentPurchase>() }
    val purchaseAdapter by lazy { RecentPurchaseAdapter(mRecentPurchaseList, this) }
    val mTimeList by lazy { mutableListOf<String>() }
    val mMedicationList by lazy { mutableListOf<String>() }
    val mPharmacyList by lazy { mutableListOf<String>() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent_purchase, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        setUpAdapter()
        ivBack.setOnClickListener { requireActivity().onBackPressed() }
        spinnerTime.onItemSelectedListener = this
        spinnerMedication.onItemSelectedListener = this
        spinnerPharmacy.onItemSelectedListener = this
    }

    private fun setUpAdapter() {
        mTimeList.add("Time")
        mMedicationList.add("Medication")
        mPharmacyList.add("Pharmacy")
        mTimeAdapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, mTimeList)
        mMedicationAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_dropdown_item_1line,
            mMedicationList
        )
        mPharmacyAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_dropdown_item_1line,
            mPharmacyList
        )
        spinnerTime.adapter = mTimeAdapter
        spinnerMedication.adapter = mMedicationAdapter
        spinnerPharmacy.adapter = mPharmacyAdapter

        rvRecentPur.adapter = purchaseAdapter
        rvRecentPur.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if (count > 8)
//                    if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == mRecentPurchaseList.size - 2) {
//                        getRecentPurchases()
//                    }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            }
        })

    }

    private fun getRecentPurchases() {
        timeStr =
            if (spinnerTime.selectedItem as String == "Time") "" else spinnerTime.selectedItem as String
        medStr =
            if (spinnerMedication.selectedItem as String == "Medication") "" else spinnerMedication.selectedItem as String
        pharStr =
            if (spinnerPharmacy.selectedItem as String == "Pharmacy") "" else spinnerPharmacy.selectedItem as String
        if (isInternetConnected())
            presenter.recentPurchases(timeStr, pharStr, timeStr)
    }

    override fun onRecentPurchaseResp(it: RecentPurchaseEntity) {
        mTimeList.clear()
        mMedicationList.clear()
        mPharmacyList.clear()
        mTimeList.add("Time")
        mMedicationList.add("Medication")
        mPharmacyList.add("Pharmacy")
        var pos = 0
        it.data.time.forEachIndexed { index, s ->
            if (timeStr == s) pos = index+1
            mTimeList.add(s)
        }
        if (pos!=0)
        spinnerTime.setSelection(pos)
        pos = 0 // reset
        it.data.medication.forEachIndexed { index, s ->
            if (medStr == s) pos = index+1
            mMedicationList.add(s)
        }
        if (pos!=0)
        spinnerMedication.setSelection(pos)
        pos = 0 // reset
        it.data.pharmacy.forEachIndexed { index, s ->
            if (pharStr == s) pos = index+1
            mPharmacyList.add(s)
        }
        if (pos!=0)
        spinnerPharmacy.setSelection(pos)
        mTimeAdapter.notifyDataSetChanged()
        mMedicationAdapter.notifyDataSetChanged()
        mPharmacyAdapter.notifyDataSetChanged()

        mRecentPurchaseList.clear()
        mRecentPurchaseList.addAll(it.data.purchases)
        purchaseAdapter.notifyDataSetChanged()
        try {
            try {
                if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                    decodeToken(it.auth.new_jwt_token) {  }
            } catch (e: Exception) {
            }
            count = it.count
        } catch (e: Exception) {
        }
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (SystemClock.elapsedRealtime() - GBRxApp.lastTimeClicked > GBRxApp.ON_CLICK_DELAY) {
            getRecentPurchases()
        }
        GBRxApp.lastTimeClicked = SystemClock.elapsedRealtime()
    }

    override fun onSingleListClick(item: Any, position: Int) {

    }

}