package com.app.givebackrx.appcode.selectaddress.paymentsModule

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.givebackrx.R
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.orderSummary.addPaymentModule.AddNewPaymentFragment
import com.app.givebackrx.appcode.payment.IPaymentView
import com.app.givebackrx.appcode.selectaddress.addnewaddress.AddNewAddressFragment
import com.app.givebackrx.appcode.storecheckout.StoreCheckoutFragment
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.*
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.AllinOneDialog
import com.app.givebackrx.utils.extension.addMainFragment
import com.app.givebackrx.utils.extension.onOffVisibility
import com.app.givebackrx.utils.extension.toast
import kotlinx.android.synthetic.main.fragment_select_address.*
import kotlinx.android.synthetic.main.fragment_select_address.ivBack
import javax.inject.Inject


class SelectPaymentsFragment : BaseFragment(), ISelectPaymentsView, (String, Int) -> Unit {

    var screen_type=""
    var screen_type_two=""
    var catalog_order_id=""
    var broad=""
    @Inject
    lateinit var presenter: SelectPaymentPresenter<ISelectPaymentsView>

    val isPortalUser by lazy {
        mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).contains("charity").not()
    }
    var mAllAddressListData = mutableListOf<DataPay>()

    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }

    var alertDialog: AlertDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_address, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
//        if (isInternetConnected())

        ivBack.setOnClickListener { requireActivity().onBackPressed() }

        rvAddress.adapter = SelectPaymentsAdapter(mAllAddressListData, this)

        val frag_value = arguments
        if (frag_value!!.containsKey("screentype_two")) {
            screen_type_two=frag_value.getString("screentype_two").toString()
            catalog_order_id=frag_value.getString("catalog_order_id").toString()
        }
        if (frag_value!!.containsKey("screentype")) {
            screen_type= frag_value.getString("screentype").toString()
            catalog_order_id=frag_value.getString("catalog_order_id").toString()
                tvTitle.text="Payment Types"
                if (isInternetConnected()) presenter.getAllAddress()


        }



        btnAddAddress.setOnClickListener {

            val args = Bundle()
            args.putString("screentype", screen_type)
            val addNewPaymentFragment = AddNewPaymentFragment()
            addNewPaymentFragment.setArguments(args)

            mainActivity.addMainFragment(R.id.homeContainer, addNewPaymentFragment)

//            mainActivity.addMainFragment(
//                R.id.homeContainer,
//                AddNewAddressFragment()
//            )
        }
    }

    override fun onAllPaymentRes(it: PaymentResEntity) {
        mAllAddressListData.clear()
        mAllAddressListData.addAll(it.data)
        btnAddAddress.onOffVisibility(mAllAddressListData.isEmpty())
        rvAddress.adapter!!.notifyDataSetChanged()

        if (broad.equals("true")){
            if (screen_type_two.isNotEmpty()){
                LocalBroadcastManager.getInstance(requireActivity())
                    .sendBroadcast(Intent("ADDRESS_CHANGED_REVIEW").apply { putExtra("Changed", "true") })
                requireActivity().onBackPressed()
            }else{

                LocalBroadcastManager.getInstance(requireActivity())
                    .sendBroadcast(Intent("ADDRESS_CHANGED_BILLING").apply { putExtra("Changed", "true") })
                requireActivity().onBackPressed()
            }
        }
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

    override fun invoke(p1: String, p2: Int) {
        when (p1) {
            "default" -> {
                if (isInternetConnected())
                    presenter.defaultPayment(catalog_order_id,mAllAddressListData[p2].card_id,screen_type, p2)
            }
            "edit" -> {
//                mDefaultAddress=mAllAddressListData[p2]
                mainActivity.addMainFragment(
                R.id.homeContainer,
                AddNewAddressFragment()
            )
            }
            "add" -> {

                val args = Bundle()
                args.putString("screentype", screen_type)
                val addNewPaymentFragment = AddNewPaymentFragment()
                addNewPaymentFragment.setArguments(args)

                mainActivity.addMainFragment(R.id.homeContainer, addNewPaymentFragment)

            }
            "delete" -> {
                AllinOneDialog("Delete Payment Card",
                    "Would you like to delete this card ?",
                    "No", "Yes", {}, {
                        if (isInternetConnected())
                            presenter.deletePayment(mAllAddressListData[p2].card_id, p2)
                    })
            }
        }
    }



    override fun onDeleteAddressResp(
        it: SuccessMessageEntity,
        pos: Int
    ) {
        mAllAddressListData.removeAt(pos)
        rvAddress.adapter!!.notifyDataSetChanged()
        btnAddAddress.onOffVisibility(mAllAddressListData.isEmpty())
    }

    override fun onDefaultAddressResp(
        it: SuccessMessageEntity,
        pos: Int
    ) {
        toast(it.message, true)
        mAllAddressListData.forEach {
            it.selected_card = false
        }
        mAllAddressListData[pos].selected_card = true
        rvAddress.adapter!!.notifyDataSetChanged()

        if (screen_type_two.isNotEmpty()){
            LocalBroadcastManager.getInstance(requireActivity())
                    .sendBroadcast(Intent("ADDRESS_CHANGED_REVIEW").apply { putExtra("Changed", "true") })
            requireActivity().onBackPressed()
        }else{

                LocalBroadcastManager.getInstance(requireActivity())
                        .sendBroadcast(Intent("ADDRESS_CHANGED_BILLING").apply { putExtra("Changed", "true") })
                requireActivity().onBackPressed()
        }





//        StoreCheckoutFragment.storeData!!.data.default_address = mAllAddressListData[pos].default_address
    }


    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
            broardReceiver,
            IntentFilter("Update_addresses")
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(broardReceiver)
    }

    val broardReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1!!.hasExtra("Added")) {
                if (p1.getStringExtra("Added").equals("true")) {
                    broad="true"
                    if (isInternetConnected())
                     presenter.getAllAddress()

                }
            }
        }
    }
}