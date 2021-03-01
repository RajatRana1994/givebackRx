package com.app.givebackrx.appcode.settings.changePasswordModule


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.givebackrx.R
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.NotificationEntity
import com.app.givebackrx.data.entity.SecurityDetailEntity
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.utils.AppConstants.PATTERN
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.toast
import kotlinx.android.synthetic.main.fragment_change_password.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ChangePasswordFragment : BaseFragment(), IChangePasswordView {

    @Inject
    lateinit var presenter: ChangePasswordPresenter<IChangePasswordView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)

        ivBack.setOnClickListener { requireActivity().onBackPressed() }

        btnResest.setOnClickListener {
            /*if (etCurrentPassword.text.isEmpty()) {
                toast("Enter Current Password", false)
            } else*/ if (etNewPassword.text.isEmpty()) {
                toast(getString(R.string.enter_new_password), false)
            } else if (!PATTERN.matcher(etNewPassword.text.toString().trim()).matches()) {
                toast(
                    getString(R.string.password_length_message),
                    false
                )
            } else if (etNewPassword.text.toString().equals(etConfirmPassword.text.toString())
                    .not()
            ) {
                toast(getString(R.string.password_notmatch), false)
            } else {
                if (isInternetConnected())
                    presenter.changePassword(
                        etNewPassword.text.toString()
                    )
            }
        }

    }

    override fun onUpdatedSuccess(it: SuccessMessageEntity) {
        toast(it.message)
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token) { }
        } catch (e: Exception) {
        }
        etNewPassword.setText("")
        etConfirmPassword.setText("")
    }

    override fun onSecuritySuccess(it: SecurityDetailEntity) {

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
