package com.app.givebackrx.appcode.settings.favouriteModule


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.givebackrx.R
import com.app.givebackrx.appcode.drugDetail.DrugDetailFragment
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.main.home.HomeFragment
import com.app.givebackrx.appcode.main.pharmacy.PharmacyFragment
import com.app.givebackrx.appcode.payment.PaymentFragment
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.FavoriteMedication
import com.app.givebackrx.data.entity.FavoriteMedicationEntity
import com.app.givebackrx.listeners.PairListCLickListener
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.AllinOneDialog
import com.app.givebackrx.utils.extension.addMainFragment
import com.app.givebackrx.utils.extension.toast
import com.app.givebackrx.utils.extension.upgradeCard
import kotlinx.android.synthetic.main.fragment_faviurites.*
import javax.inject.Inject


class FavouritesFragment : BaseFragment(), IFavouriteView, PairListCLickListener,
    AdapterView.OnItemSelectedListener {

    val favMedList = mutableListOf<FavoriteMedication>()
    val mFavouriteAdapter by lazy { FavouritesAdapter(this, favMedList,mPrefs) }
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }

    //    var count=0
    @Inject
    lateinit var presenter: FavouritePresenter<IFavouriteView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faviurites, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        rvFavourite.adapter = mFavouriteAdapter
        if (isInternetConnected())
            presenter.getFavoriteMedication(true)
        tvAdd.setOnClickListener {
            mainActivity.addMainFragment(R.id.homeContainer, HomeFragment())
        }

        ivBack.setOnClickListener { requireActivity().onBackPressed() }
    }

    override fun onFavoriteMedicationResp(it: FavoriteMedicationEntity) {
        favMedList.clear()
        favMedList.addAll(it.data.favorite_medication)
        mFavouriteAdapter.notifyDataSetChanged()
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

    override fun onFirstListClick(item: Any, position: Int) {
        if (item is String) {
            when (item) {
                "gold" -> {
                    upgradeCard {
                        val args = Bundle()
                        args.putString("from_dashboard", "no")
                        val paymentFragment = PaymentFragment()
                        paymentFragment.setArguments(args)

                        mainActivity.addMainFragment(R.id.homeContainer, paymentFragment)
                    }
                }
                "get_coupon" -> {
//                    PharmacyFragment.drug_generic_name = favMedList[position].drug_generic_name
//                    PharmacyFragment.drug_name = favMedList[position].drug_name__c
//                    mainActivity.addMainFragment(R.id.homeContainer, PharmacyFragment())

                    PharmacyFragment.drug_generic_name = favMedList[position].drug_generic_name
                    PharmacyFragment.drug_name = favMedList[position].drug_name__c
                    mainActivity.addMainFragment(R.id.homeContainer, PharmacyFragment())


//                    DrugDetailFragment.pricebook_entry_id = favMedList[position].pricebook_entry_id
//                    DrugDetailFragment.pharmacy_address_id = favMedList[position].pharmacy_address_id
//                    mainActivity.addMainFragment(R.id.homeContainer, DrugDetailFragment())
                }
            }
        }
    }

    override fun onSecondListClick(item: Any, position: Int) {
        if (item is String) {
            when (item) {
                "delete" -> {
                    AllinOneDialog(msg = "Would you like to remove this medicine from favorites?",
                        onLeftClick = {},
                        onRightClick = {
                            if (isInternetConnected())
                                presenter.deleteFavoriteMedication(
                                    favMedList[position].favorite_medication_id,
                                    position
                                )
                        })
                }
                "edit" -> {
                    DrugDetailFragment.pricebook_entry_id = favMedList[position].pricebook_entry_id
                    DrugDetailFragment.pharmacy_address_id = favMedList[position].pharmacy_address_id
                    mainActivity.addMainFragment(R.id.homeContainer, DrugDetailFragment())
                }
            }
        }
    }

    override fun onDeleteFavoriteMedicationResp(
        it: FavoriteMedicationEntity,
        position: Int
    ) {
        if (it.success) {
            toast(it.message, true)
            favMedList.removeAt(position)
            mFavouriteAdapter.notifyItemRemoved(position)
        } else toast(it.message, false)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }


    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
            broardReceiver,
            IntentFilter("UPDATE_FAVS")
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(broardReceiver)
    }

    val broardReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1!!.hasExtra("updated")) {
                if (p1.getBooleanExtra("updated", true)) {
                    if (isInternetConnected())
                        presenter.getFavoriteMedication(false)

                }
            }
        }
    }
}
