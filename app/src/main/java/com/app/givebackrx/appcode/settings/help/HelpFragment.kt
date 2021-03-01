package com.app.givebackrx.appcode.settings.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.app.givebackrx.R
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.FaqItem
import com.app.givebackrx.data.entity.HelpEntity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.listeners.SingleListCLickListener
import com.app.givebackrx.utils.PreferenceConstants
import kotlinx.android.synthetic.main.fragment_help.*
import java.lang.Exception
import javax.inject.Inject

class HelpFragment : BaseFragment(), SingleListCLickListener, IHelpView,
    AdapterView.OnItemSelectedListener {

    val helpList= mutableListOf<FaqItem>()
    val mHelpAdapter by lazy { HelpAdapter(helpList,this) }

    @Inject
    lateinit var presenter: HelpPresenter<IHelpView>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        spinnerHelp.onItemSelectedListener = this
        rvHelp.adapter=mHelpAdapter
        ivBack.setOnClickListener { requireActivity().onBackPressed() }
    }


    override fun onHelpResp(it: HelpEntity) {
        if (it.data!=null) {
            helpList.clear()
            helpList.addAll(it.data.faq)
            mHelpAdapter.notifyDataSetChanged()
        }
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token){}
        }catch (e: Exception){}
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

    override fun onSingleListClick(item: Any, position: Int) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onHelpTicketResp(it: SignInWithUserDetailEntity) {

    }
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (isInternetConnected())
            if (spinnerHelp.selectedItemPosition == 0){
            presenter.getFaq("false", "false", "false", "false")
        }else{
            presenter.getFaq(
                if (spinnerHelp.selectedItemPosition == 1) "true" else "false",
                if (spinnerHelp.selectedItemPosition == 2) "true" else "false",
                if (spinnerHelp.selectedItemPosition == 3) "true" else "false",
                if (spinnerHelp.selectedItemPosition == 4) "true" else "false"
            )
        }
    }

}