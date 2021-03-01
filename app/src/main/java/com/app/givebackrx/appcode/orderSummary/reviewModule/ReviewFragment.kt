package com.app.givebackrx.appcode.orderSummary.reviewModule

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
import com.app.givebackrx.appcode.orderSummary.billingModule.BillingFragment
import com.app.givebackrx.appcode.orderSummary.thankYouModule.ThankYouFragment
import com.app.givebackrx.appcode.selectaddress.SelectAddressFragment
import com.app.givebackrx.appcode.selectaddress.paymentsModule.SelectPaymentsFragment
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.*
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.addMainFragment
import com.app.givebackrx.utils.extension.toast
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_revieworder.*
import kotlinx.android.synthetic.main.fragment_revieworder.btnCheckout
import kotlinx.android.synthetic.main.fragment_revieworder.ivBack
import kotlinx.android.synthetic.main.fragment_revieworder.mTvShipAddress
import kotlinx.android.synthetic.main.fragment_revieworder.tvEstimatedTax
import kotlinx.android.synthetic.main.fragment_revieworder.tvPriceTagDiscount
import kotlinx.android.synthetic.main.fragment_revieworder.tvShipping
import kotlinx.android.synthetic.main.fragment_revieworder.tvSubtotal
import kotlinx.android.synthetic.main.fragment_revieworder.tvTotal
import javax.inject.Inject


class ReviewFragment : BaseFragment(), IReviewView {

    var orderSummary: StoreOrderSummary? = null
    var catalog_order_id = ""
    var service_id = ""
    var service_name = ""
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }

    @Inject
    lateinit var presenter: ReviewPresenter<IReviewView>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_revieworder, container, false)
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
        if (frag_value!!.containsKey("service_id")) {
            service_id=frag_value.getString("service_id").toString()
            service_name=frag_value.getString("service_name").toString()
        }

        if (isInternetConnected()) {
            presenter.getOrderSummaryService(mPrefs.getKeyValue(PreferenceConstants.USER_TYPE),service_id)
        }
        ivBack.setOnClickListener { requireActivity().onBackPressed() }
        mTvEditShipping.setOnClickListener {
            val args = Bundle()
            args.putString("screentype", "Shipping")
            args.putString("screentype_two", "Review")
            val selectAddressFragment = SelectAddressFragment()
            selectAddressFragment.setArguments(args)
            mainActivity.addMainFragment(R.id.homeContainer, selectAddressFragment)
        }
        mTvEditBilling.setOnClickListener {
            val args = Bundle()
            args.putString("screentype", "Billing")
            args.putString("screentype_two", "Review")
            val selectAddressFragment = SelectAddressFragment()
            selectAddressFragment.setArguments(args)
            mainActivity.addMainFragment(R.id.homeContainer, selectAddressFragment)
        }
        mTvEditPayment.setOnClickListener {
            val args = Bundle()
            args.putString("screentype", "Review")
            args.putString("screentype_two", "Review")
            args.putString("catalog_order_id", catalog_order_id)
            val selectPaymentsFragment = SelectPaymentsFragment()
            selectPaymentsFragment.setArguments(args)
            mainActivity.addMainFragment(R.id.homeContainer, selectPaymentsFragment)
        }

        btnCheckout.setOnClickListener {

            if (orderSummary!!.data.shopping_type!!.toLowerCase().equals("charity")) {
                if (orderSummary!!.data.shipping_address!!.address_id.isEmpty()) {
                    toast("Please select shipping address")
                } else {
                    val json = JsonObject().apply {
                        addProperty("catalog_order_id", orderSummary!!.data.product_list.catalog_order_id)
                        addProperty("shipping_method", service_name)
                        addProperty("shipping_method_id", service_id)
                        addProperty("shopping_type", orderSummary!!.data.shopping_type)
                    }

                    if (isInternetConnected()) {
                        presenter.storePayment(json)
                    }
                }
            } else {
                if (orderSummary!!.data.shipping_address!!.address_id == null) {
                    toast("Select shipping address")
                } else if (service_id.isEmpty()) {
                    toast("Select shipping method")
                } else {
                    if (orderSummary!!.data.shipping_address!!.address_id.isEmpty()) {
                        toast("Please select shipping address")
                    } else if (orderSummary!!.data.billing_address!!.address_id.isEmpty()) {
                        toast("Please select billing address")
                    } else if (orderSummary!!.data.payment_method!!.card_id.isEmpty()) {
                        toast("Please select payment card")
                    } else {
                        val json = JsonObject().apply {
                            addProperty("catalog_order_id", orderSummary!!.data.product_list.catalog_order_id)
                            addProperty("shipping_method", service_name)
                            addProperty("shipping_method_id", service_id)
                            addProperty("shopping_type", orderSummary!!.data.shopping_type)
                        }

                        if (isInternetConnected()) {
                            presenter.storePayment(json)
                        }
                    }

                }
            }



        }

    }

    override fun onOrderStoreSummary(it: StoreOrderSummary) {
        this.orderSummary = it

        if (it.data.shopping_type.equals("charity")) {
            lblBill.visibility=View.GONE
            mTvEditBilling.visibility=View.GONE
            mTvBillAddress.visibility=View.GONE
            lblPayment.visibility=View.GONE
            mTvEditPayment.visibility=View.GONE
            mTvPayType.visibility=View.GONE
            mTvChangePay.visibility=View.GONE
        }else{
            lblBill.visibility=View.VISIBLE
            mTvEditBilling.visibility=View.VISIBLE
            mTvBillAddress.visibility=View.VISIBLE
            lblPayment.visibility=View.VISIBLE
            mTvEditPayment.visibility=View.VISIBLE
            mTvPayType.visibility=View.VISIBLE
            mTvChangePay.visibility=View.GONE
        }


        if (it.data.hold_response != false) {
            if (isInternetConnected()) {
                presenter.getOrderSummaryService(mPrefs.getKeyValue(PreferenceConstants.USER_TYPE),service_id)
            }
        } else {
            clparentReview.visibility = View.VISIBLE
            mTvShipAddress.text = it.data.shipping_address!!.first_name + " " + it.data.shipping_address.last_name + "\n" + if (it.data.shipping_address.apt_suit_number.isNotEmpty()) {
                it.data.shipping_address.apt_suit_number + ", " + it.data.shipping_address.address + ",\n" + it.data.shipping_address.city + ", " + it.data.shipping_address.state + ", " + it.data.shipping_address.country + ",\n" + it.data.shipping_address.phone
            } else {
                it.data.shipping_address.address + ",\n" + it.data.shipping_address.city + ", " + it.data.shipping_address.state + ", " + it.data.shipping_address.country + ",\n" + it.data.shipping_address.phone
            }
            mTvBillAddress.text = it.data.billing_address!!.first_name + " " + it.data.billing_address.last_name + "\n" + if (it.data.billing_address.apt_suit_number.isNotEmpty()) {
                it.data.billing_address.apt_suit_number + ", " + it.data.billing_address.address + ",\n" + it.data.billing_address.city + ", " + it.data.billing_address.state + ", " + it.data.billing_address.country + ",\n" + it.data.billing_address.phone
            } else {
                it.data.billing_address.address + ",\n" + it.data.billing_address.city + ", " + it.data.billing_address.state + ", " + it.data.billing_address.country + ",\n" + it.data.billing_address.phone
            }

            if (it.data.payment_method!!.first_name != null) {
                mTvPayType.text = it.data.payment_method.first_name + " " + it.data.payment_method.last_name + "\n" + it.data.payment_method.card_number + "\n" + "Exp. " + it.data.payment_method.expiry_month + "/" + it.data.payment_method.expiry_year
            } else {
                mTvPayType.text = "Select card for payments"
            }
            catalog_order_id = it.data.product_list.catalog_order_id
            tvSubtotal.text = it.data.order_summary.sub_total
            tvShipping.text = it.data.order_summary.shipping
            tvEstimatedTax.text = it.data.order_summary.tax
            tvPriceTagDiscount.text = it.data.order_summary.price_tag_discount
            tvTotal.text = it.data.order_summary.total
        }

    }

    override fun onOrderStorePayment(it: SuccessMessageEntity) {
        toast(it.message)
        if (isInternetConnected()) {
            presenter.storePaymentHold(orderSummary!!.data.product_list.catalog_order_id)
        }
    }

    override fun onOrderStorePaymentHold(it: HoldResponseEntity) {
        if (it.data.payment_response != true) {
            toast(it.message)
            val args = Bundle()
            args.putString("total", orderSummary!!.data.order_summary.total)
            args.putString("order_id", it.data.order_id)
//            args.putString("orderid", orderSummary.data.order_summary.)
            val thankYouFragment = ThankYouFragment()
            thankYouFragment.setArguments(args)
            mainActivity.addMainFragment(R.id.homeContainer, thankYouFragment)
        } else {
            if (isInternetConnected()) {
                presenter.storePaymentHold(orderSummary!!.data.product_list.catalog_order_id)
            }
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
                IntentFilter("ADDRESS_CHANGED_REVIEW")
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
                        presenter.getOrderSummaryService(mPrefs.getKeyValue(PreferenceConstants.USER_TYPE),service_id)
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


}