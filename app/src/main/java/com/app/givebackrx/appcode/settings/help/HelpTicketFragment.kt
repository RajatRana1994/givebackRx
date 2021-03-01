package com.app.givebackrx.appcode.settings.help

import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.givebackrx.R
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.HelpEntity
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.toast
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_help_ticket.*
import javax.inject.Inject

class HelpTicketFragment : BaseFragment(), IHelpView{

    @Inject
    lateinit var presenter: HelpPresenter<IHelpView>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help_ticket, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        ivBack.setOnClickListener { requireActivity().onBackPressed() }
        btnSubmit.setOnClickListener {
            if (edtName.text.isEmpty()) toast("Enter Name", false)
            else if (edtEmail.text.isEmpty()) toast("Enter Email", false)
            else if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.text)
                    .matches()
            ) toast("Enter Valid Email", false)
            else if (edtQues.text.isEmpty()) toast("Enter Your Question", false)
            else {
                if (isInternetConnected())
                    JsonObject().apply {
                        addProperty("name", edtName.text.toString())
                        addProperty("email", edtEmail.text.toString())
                        addProperty("category", spinnerQuesCategory.selectedItem.toString())
                        addProperty("description", edtQues.text.toString())
                        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
                        addProperty("membership_type", mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE))
                        presenter.helpTicket(this)
                    }
            }
        }
    }


    override fun onHelpTicketResp(it: SignInWithUserDetailEntity) {
        Handler().postDelayed({requireActivity().onBackPressed()},1000)
        toast(it.message)

    }
    override fun onHelpResp(it: HelpEntity) {

    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

}