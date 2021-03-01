package com.app.givebackrx.appcode.settings.mycharity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.givebackrx.R
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.main.charity.CharityFragment
import com.app.givebackrx.appcode.main.charitydetail.CharityDetailFragment
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.MyCharityEntity
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.addMainFragment
import com.app.givebackrx.utils.extension.onOffVisibility
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_my_charity.*
import javax.inject.Inject

class MyCharityFragment : BaseFragment(), IMyCharityView {

    @Inject
    lateinit var presenter: MyCharityPresenter<IMyCharityView>


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
        return inflater.inflate(R.layout.fragment_my_charity, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)

        if (isInternetConnected())
            presenter.myCharity()
        txtData.text =
            Html.fromHtml("give" + "<font><b>" + "back" + "</b></font>" + "Rx's "+getString(R.string.totaldonations))
         btnSelectNewCharity.setOnClickListener {

             val args = Bundle()
             args.putString("from_dashboard", "yes")
             val charityFragment = CharityFragment()
             charityFragment.setArguments(args)
            mainActivity.addMainFragment(R.id.homeContainer, charityFragment)
        }
        ivBack.setOnClickListener {
            mainActivity.onBackPressed()
        }
        ivSearch.setOnClickListener {
            edtSearch.onOffVisibility(edtSearch.visibility==View.GONE)
        }

        clParent.setOnClickListener {  }
    }

    override fun onMyCharityResp(charity: MyCharityEntity) {
        parentLayout.onOffVisibility(true)
        try {
            tvCharityName.text = charity.data[0].charity_name
            tvDescription.text = charity.data[0].description
            tvamount.text = if(charity.data[0].donation_from_giveback_enterprises.isNotEmpty()) charity.data[0].donation_from_giveback_enterprises else "$0.00"
            if (!charity.data[0].charity_logo.isEmpty()) {
                Glide.with(context).load(charity.data[0].charity_logo).into(ivCharity);
            }
            tvViewMore.setOnClickListener {
                val frag = CharityDetailFragment()
                frag.arguments = Bundle().apply {
                    putString("charity_id_extra", charity.data[0].charity_id)
                    putString("charity_name_extra", charity.data[0].charity_name)
                }
                mainActivity.addMainFragment(R.id.homeContainer, frag)
            }
            try {
                if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                    decodeToken(charity.auth.new_jwt_token) {  }
            } catch (e: Exception) {
            }
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
            IntentFilter("UPDATE_CHARITY")
        ) 
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
            broardReceiver,
            IntentFilter("UPDATE_USER_CHARITY")
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
                        presenter.myCharity()
                }
            }
        }
    }
}