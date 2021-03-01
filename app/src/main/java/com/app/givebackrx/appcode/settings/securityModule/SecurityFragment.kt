package com.app.givebackrx.appcode.settings.securityModule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.app.givebackrx.R
import com.app.givebackrx.appcode.settings.changePasswordModule.ChangePasswordPresenter
import com.app.givebackrx.appcode.settings.changePasswordModule.IChangePasswordView
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.NotificationEntity
import com.app.givebackrx.data.entity.SecurityDetailEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.utils.extension.toast
import kotlinx.android.synthetic.main.fragment_security.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SecurityFragment : BaseFragment(), IChangePasswordView {

    @Inject
    lateinit var presenter: ChangePasswordPresenter<IChangePasswordView>

    override fun setUp(view: View) {
        presenter.onAttach(this)
        if (isInternetConnected())
            presenter.profileSecurity()

        ivBack.setOnClickListener{activity!!.onBackPressed()}
        switch2Fa.setOnClickListener{
            if (isInternetConnected())
                presenter.profileEnableAuth(switch2Fa.isChecked.not().toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_security, container, false)
    }

    override fun onUpdatedSuccess(it: SuccessMessageEntity) {
        switch2Fa.isChecked=!switch2Fa.isChecked
        toast(it.message)

    }

    override fun onSecuritySuccess(it: SecurityDetailEntity) {
        switch2Fa.isChecked=it.data.two_factor_authentication

    }

    override fun OnNotificationGetResp(it: NotificationEntity) {

    }

    override fun OnNotificationResp(it: SuccessMessageEntity, type: Int) {

    }

    override fun onGeneratedToken(lastAction: String) {
        
    }

    override fun enableButton() {
        
    }

    override fun disableButton() {
        
    }


}
