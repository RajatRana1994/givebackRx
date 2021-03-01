package com.app.givebackrx.appcode.orderSummary.shippingModule

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.InputFilter
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.givebackrx.R
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.mycart.MyCartAdapter
import com.app.givebackrx.appcode.orderSummary.billingModule.BillingFragment
import com.app.givebackrx.appcode.orderSummary.reviewModule.ReviewFragment
import com.app.givebackrx.appcode.payment.CardValidator
import com.app.givebackrx.appcode.payment.PaymentFragment
import com.app.givebackrx.appcode.selectaddress.SelectAddressFragment
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.*
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_shipping.*
import kotlinx.android.synthetic.main.fragment_shipping.btnCheckout
import kotlinx.android.synthetic.main.fragment_shipping.ivBack
import kotlinx.android.synthetic.main.fragment_shipping.tvEstimatedTax
import kotlinx.android.synthetic.main.fragment_shipping.tvPriceTagDiscount
import kotlinx.android.synthetic.main.fragment_shipping.tvShipping
import kotlinx.android.synthetic.main.fragment_shipping.tvSubtotal
import kotlinx.android.synthetic.main.fragment_shipping.tvTotal
import javax.inject.Inject


class ShippingFragment : BaseFragment(), IShippmentView, (String, Int) -> Unit {

    var orderSummary: StoreOrderSummary? = null
    var coupon_info = ""
    var service_id = ""
    var service_name = ""
    val mSavedCard = mutableListOf<SavedCard>()


    val mShippingMethodList = mutableListOf<ShippingMethodList>()
    val mMyCartAdapter by lazy { SelectShippingAdapter(mShippingMethodList, this) }
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }

    @Inject
    lateinit var presenter: ShipmentPresenter<IShippmentView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shipping, container, false)
    }


    override fun setUp(view: View) {
        presenter.onAttach(this)
        val frag_value = arguments
//        if (frag_value!!.containsKey("sixforone")) {
//            etCoupon.setText("6FOR1")
//            if (isInternetConnected()) {
//                presenter.goldCardPaymentSummary("6FOR1")
//            }
//        }

        rvCartItemList.adapter = mMyCartAdapter
        if (isInternetConnected()) {
            presenter.getOrderSummary(mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
        }
        ivBack.setOnClickListener { requireActivity().onBackPressed() }
        mTvChangeAddress.setOnClickListener {
            val args = Bundle()
            args.putString("screentype", "Shipping")
            val selectAddressFragment = SelectAddressFragment()
            selectAddressFragment.setArguments(args)
            mainActivity.addMainFragment(R.id.homeContainer, selectAddressFragment)
        }
        btnCheckout.setOnClickListener {
            if (orderSummary!!.data.shopping_type!!.toLowerCase().equals("charity")) {
                if (orderSummary!!.data.shipping_address!!.address_id == null) {
                    toast("Select shipping address")
                } else {
                    val args = Bundle()
                    args.putString("service_id", service_id)
                    args.putString("service_name", service_name)
                    val reviewFragment = ReviewFragment()
                    reviewFragment.setArguments(args)
                    mainActivity.addMainFragment(R.id.homeContainer, reviewFragment)
                }
            } else {
                if (orderSummary!!.data.shipping_address!!.address_id == null) {
                    toast("Select shipping address")
                } else if (service_id.isEmpty()) {
                    toast("Select shipping method")
                } else {
                    if (orderSummary!!.data.shopping_type!!.toLowerCase().equals("charity")) {
                        val args = Bundle()
                        args.putString("service_id", service_id)
                        args.putString("service_name", service_name)
                        val reviewFragment = ReviewFragment()
                        reviewFragment.setArguments(args)
                        mainActivity.addMainFragment(R.id.homeContainer, reviewFragment)
                    } else {
                        val args = Bundle()
                        args.putString("service_id", service_id)
                        args.putString("service_name", service_name)
                        val billingFragment = BillingFragment()
                        billingFragment.setArguments(args)
                        mainActivity.addMainFragment(R.id.homeContainer, billingFragment)
                    }

                }
            }


        }

    }


    override fun onOrderStoreSummary(it: StoreOrderSummary) {

        this.orderSummary = it
        if (it.data.shopping_type.equals("charity")) {
            clCoupon.visibility = View.GONE
            btnCheckout.text = "Continue to Order"
        } else {
            clCoupon.visibility = View.VISIBLE
            btnCheckout.text = "Continue to Payment"
        }
        if (it.data.hold_response != false) {
            if (isInternetConnected()) {
                presenter.getOrderSummary(mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
            }
        } else {
            clParentShip.visibility = View.VISIBLE
            if (it.data.shipping_address!!.address_id == null) {
                mTvShipAddress.text = "Select shipping address"
            } else {
                mTvShipAddress.text =
                    it.data.shipping_address.first_name + " " + it.data.shipping_address.last_name + "\n" + if (it.data.shipping_address.apt_suit_number.isNotEmpty()) {
                        it.data.shipping_address.apt_suit_number + ", " + it.data.shipping_address.address + ",\n" + it.data.shipping_address.city + ", " + it.data.shipping_address.state + ", " + it.data.shipping_address.country + ",\n" + it.data.shipping_address.phone
                    } else {
                        it.data.shipping_address.address + ",\n" + it.data.shipping_address.city + ", " + it.data.shipping_address.state + ", " + it.data.shipping_address.country + ",\n" + it.data.shipping_address.phone
                    }
            }


            if (it.data.shipping_method_list!!.isNotEmpty() || it.data.shipping_method_list!=null) {
                mShippingMethodList.clear()
                mShippingMethodList.addAll(it.data.shipping_method_list)
                mMyCartAdapter.notifyDataSetChanged()
            }


            tvSubtotal.text = it.data.order_summary.sub_total
            tvShipping.text = it.data.order_summary.shipping
            tvEstimatedTax.text = it.data.order_summary.tax
            tvPriceTagDiscount.text = it.data.order_summary.price_tag_discount
            tvTotal.text = it.data.order_summary.total
        }
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
            broardReceiver,
            IntentFilter("ADDRESS_CHANGED")
        )

        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
            broardReceiverTwo,
            IntentFilter("GO_BACK")
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(broardReceiver)
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(broardReceiverTwo)
    }

    val broardReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1!!.hasExtra("Changed")) {
                if (p1.getStringExtra("Changed").equals("true")) {
                    if (isInternetConnected()) {
                        presenter.getOrderSummary(mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                    }
                }
            }
        }
    }

    val broardReceiverTwo = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(Intent("GO_BACK"))
            requireActivity().onBackPressed()
        }
    }

    override fun invoke(p1: String, p2: Int) {
        if (isInternetConnected()) {
            service_id = mShippingMethodList[p2].id
            service_name = mShippingMethodList[p2].lable
            presenter.getOrderSummaryService(
                mPrefs.getKeyValue(PreferenceConstants.USER_TYPE),
                mShippingMethodList[p2].id
            )
        }

    }

}