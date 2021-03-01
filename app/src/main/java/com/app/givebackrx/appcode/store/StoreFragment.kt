package com.app.givebackrx.appcode.store

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.givebackrx.R
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.mycart.MyCartFragment
import com.app.givebackrx.appcode.printedcard.PrintedCardFragment
import com.app.givebackrx.appcode.storedetail.StoreDetailFragment
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.StoreListEntity
import com.app.givebackrx.data.entity.StoreProduct
import com.app.givebackrx.localdb.LocalCartDataBs
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.addMainFragment
import com.app.givebackrx.utils.extension.onOffVisibility
import kotlinx.android.synthetic.main.fragment_store.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoreFragment : BaseFragment(), IStoreView {
    val localCart by lazy { LocalCartDataBs.localCartDb(requireActivity()).localCartDao() }

    val mStoreList = mutableListOf<StoreProduct>()
    val mStoreAdapter by lazy {
        StoreAdapter(mStoreList) {
            mainActivity.addMainFragment(R.id.homeContainer, StoreDetailFragment().apply {
                arguments = Bundle().apply {
                    putString("product_group_extra", mStoreList[it].product_group)
                    putString("category_extra", mStoreList[it].category)
                    putString("color_extra", mStoreList[it].color)
                    putString("size_extra", mStoreList[it].size)
                }
            })
        }
    }

    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }

    //https://api-uat.giveback-rx.com/v1/storeProductDetail?category=T-Shirt&product_group=Tee01-Black&color=Black&size=XXL
    @Inject
    lateinit var presenter: StorePresenter<IStoreView>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        rvStore.adapter = mStoreAdapter
        ivBack.setOnClickListener { requireActivity().onBackPressed() }
        btnPrintedCard.setOnClickListener { mainActivity.addMainFragment(R.id.homeContainer, PrintedCardFragment()) }
        tvSkip.setOnClickListener {
            mainActivity.addMainFragment(
                    R.id.homeContainer,
                    MyCartFragment()
            )
        }
        HashMap<String, String>().apply {
            put("category", "T-Shirt")
            put("page_number", "1")
            if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
                put("membership_type", mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE))
            presenter.storeListing(this)
        }
    }

    override fun onStoreListResp(it: StoreListEntity) {
        if (it.data.pages.isNotEmpty())
            if (it.data.pages[0] == 1) mStoreList.clear()
        mStoreList.addAll(it.data.products)
        mStoreAdapter.notifyDataSetChanged()
        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
            if (it.data.total_cart_items.isNullOrEmpty().not())
                cartBedge.apply {
                    text = it.data.total_cart_items
                    onOffVisibility(it.data.total_cart_items.toInt() > 0)
                }
        } else getCartCount()

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

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
                broardReceiver,
                IntentFilter("UPDATE_LIST")
        )
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
                broardReceiver,
                IntentFilter("UPDATE_CART")
        )

        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(broardReceiverTwo, IntentFilter("GO_BACK"))
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(broardReceiver)
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(broardReceiverTwo)
    }

    fun getCartCount() {
        GlobalScope.launch {
            launch(Dispatchers.IO) {
                val alls = localCart.getAll()
                requireActivity().runOnUiThread {
                    cartBedge.apply {
                        text = alls.size.toString()
                        onOffVisibility(alls.size > 0)
                    }
                }
            }
        }
    }

    val broardReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1!!.hasExtra("cart_updated")) {
                if (p1.getBooleanExtra("cart_updated", true)) {
                    if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
                        if (isInternetConnected())
                            HashMap<String, String>().apply {
                                put("category", "T-Shirt")
                                put("page_number", "1")
                                if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
                                    put(
                                            "membership_type",
                                            mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
                                    )
                                presenter.storeListing(this)
                            }
                    } else getCartCount()
                }
            }
        }
    }


    val broardReceiverTwo = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {

            requireActivity().onBackPressed()
        }
    }
}