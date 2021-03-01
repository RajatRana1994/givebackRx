package com.app.givebackrx.appcode.orderSummary.billingModule

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.InputFilter
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.givebackrx.R
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.orderSummary.reviewModule.ReviewFragment
import com.app.givebackrx.appcode.selectaddress.SelectAddressFragment
import com.app.givebackrx.appcode.selectaddress.paymentsModule.SelectPaymentsFragment
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.*
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.addMainFragment
import com.app.givebackrx.utils.extension.toast
import kotlinx.android.synthetic.main.fragment_billing.*
import kotlinx.android.synthetic.main.fragment_billing.btnCheckout
import kotlinx.android.synthetic.main.fragment_billing.ivBack
import kotlinx.android.synthetic.main.fragment_billing.mTvChangeAddress
import kotlinx.android.synthetic.main.fragment_billing.mTvShipAddress
import kotlinx.android.synthetic.main.fragment_billing.tvEstimatedTax
import kotlinx.android.synthetic.main.fragment_billing.tvPriceTagDiscount
import kotlinx.android.synthetic.main.fragment_billing.tvShipping
import kotlinx.android.synthetic.main.fragment_billing.tvSubtotal
import kotlinx.android.synthetic.main.fragment_billing.tvTotal
import kotlinx.android.synthetic.main.fragment_shipping.*
import javax.inject.Inject


class BillingFragment : BaseFragment(), IBillingView {

    var orderSummary: StoreOrderSummary? = null

    var coupon_info = ""
    var catalog_order_id = ""
    val mSavedCard = mutableListOf<SavedCard>()

    var service_id = ""
    var service_name = ""


    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }

    @Inject
    lateinit var presenter: BillingPresenter<IBillingView>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_billing, container, false)
    }


    override fun setUp(view: View) {
        presenter.onAttach(this)

        val frag_value = arguments
        if (frag_value!!.containsKey("service_id")) {
            service_id=frag_value.getString("service_id").toString()
            service_name=frag_value.getString("service_name").toString()
        }


        if (isInternetConnected()) {
            presenter.getOrderSummaryService(mPrefs.getKeyValue(PreferenceConstants.USER_TYPE),service_id)
        }
        ivBack.setOnClickListener { requireActivity().onBackPressed() }
        mTvChangePay.setOnClickListener {
            val args = Bundle()
            args.putString("screentype", "Billing")
            args.putString("catalog_order_id", catalog_order_id)
            val selectPaymentsFragment = SelectPaymentsFragment()
            selectPaymentsFragment.setArguments(args)
            mainActivity.addMainFragment(R.id.homeContainer, selectPaymentsFragment)
        }
        btnCheckout.setOnClickListener {
            if (orderSummary!!.data.billing_address!!.address_id==null){
                mTvShipAddress.text ="Select billing address"}else{

                val args = Bundle()
                args.putString("service_id", service_id)
                args.putString("service_name", service_name)
                val reviewFragment = ReviewFragment()
                reviewFragment.setArguments(args)
                mainActivity.addMainFragment(R.id.homeContainer, reviewFragment)
            }
        }
        mTvChangeAddress.setOnClickListener {
            val args = Bundle()
            args.putString("screentype", "Billing")
            val selectAddressFragment = SelectAddressFragment()
            selectAddressFragment.setArguments(args)
            mainActivity.addMainFragment(R.id.homeContainer, selectAddressFragment)
        }

    }

    override fun onOrderStoreSummary(it: StoreOrderSummary) {
        clParentBilling.visibility=View.VISIBLE
        this.orderSummary=it
        if (it.data.billing_address!!.address_id==null){
            mTvShipAddress.text ="Select billing address"
        }else{
            mTvShipAddress.text= it.data.billing_address!!.first_name+" "+it.data.billing_address.last_name+"\n"+ if (it.data.billing_address.apt_suit_number.isNotEmpty()){it.data.billing_address.apt_suit_number +", "+ it.data.billing_address.address+",\n"+ it.data.billing_address.city+", "+ it.data.billing_address.state+", "+ it.data.billing_address.country+",\n"+ it.data.billing_address.phone}else{it.data.billing_address.address+",\n"+ it.data.billing_address.city+", "+ it.data.billing_address.state+", "+ it.data.billing_address.country+",\n"+ it.data.billing_address.phone}
        }


        if (it.data.payment_method!!.first_name!=null){
            mTvPayType.text= it.data.payment_method.first_name+" "+it.data.payment_method.last_name+"\n"+ it.data.payment_method.card_number +"\n"+"Exp. "+ it.data.payment_method.expiry_month+"/"+ it.data.payment_method.expiry_year
        }else{
            mTvPayType.text="Select card for payments"
        }

        catalog_order_id = it.data.product_list.catalog_order_id
        tvSubtotal.text = it.data.order_summary.sub_total
        tvShipping.text = it.data.order_summary.shipping
        tvEstimatedTax.text = it.data.order_summary.tax
        tvPriceTagDiscount.text = it.data.order_summary.price_tag_discount
        tvTotal.text = it.data.order_summary.total
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

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
        mInfoBuilder.findViewById<TextView>(R.id.titleHeader).isAllCaps = false
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


    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
                broardReceiver,
                IntentFilter("ADDRESS_CHANGED_BILLING")
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