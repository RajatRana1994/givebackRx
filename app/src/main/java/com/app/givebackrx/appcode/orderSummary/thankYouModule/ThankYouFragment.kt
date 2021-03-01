package com.app.givebackrx.appcode.orderSummary.thankYouModule

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
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.*
import com.app.givebackrx.utils.PreferenceConstants
import kotlinx.android.synthetic.main.fragment_thanksorder.*
import kotlinx.android.synthetic.main.fragment_thanksorder.ivBack
import javax.inject.Inject


class ThankYouFragment : BaseFragment(),IThankView {

    var catalog_order_id = ""

    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thanksorder, container, false)
    }


    override fun setUp(view: View) {
        val frag_value = arguments
        if (frag_value!!.containsKey("total")) {
            mTvTotal.text=frag_value.getString("total").toString()
            mTvOrderId.text=frag_value.getString("order_id").toString()
        }
        mTvName.text=mPrefs.getKeyValue(PreferenceConstants.USER_NAME)

        ivBack.setOnClickListener {
            LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(Intent("GO_BACK"))
            requireActivity().onBackPressed() }
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

}