package com.app.givebackrx.appcode.editmycharity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.app.givebackrx.R
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.utils.extension.onOffVisibility
import kotlinx.android.synthetic.main.fragment_edit_my_charity.*

class EditMyCharityFragment : BaseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_my_charity, container, false)
    }

    override fun setUp(view: View) {
        btnUpdateChanges.text = "Next"
        btnUpdateChanges.setOnClickListener(this)
        spinnerPaymentMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                lblRoutingNumber.onOffVisibility(spinnerPaymentMethod.selectedItemPosition == 0)
                etRoutingNumber.onOffVisibility(spinnerPaymentMethod.selectedItemPosition == 0)
                lblAccountNumber.onOffVisibility(spinnerPaymentMethod.selectedItemPosition == 0)
                etAccountNumber.onOffVisibility(spinnerPaymentMethod.selectedItemPosition == 0)
            }
        }

    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

    override fun onClick(p0: View) {
        when (p0) {
            btnUpdateChanges -> {
                if (financialInfo.isChecked) {
                    //api
                } else {
                    btnUpdateChanges.text = "Update Changes"
                    financialInfo.isChecked = true
                    financialInfoLayout.onOffVisibility(true)
                    charityInfoLayout.onOffVisibility(false)
                }
            }

        }
    }

}