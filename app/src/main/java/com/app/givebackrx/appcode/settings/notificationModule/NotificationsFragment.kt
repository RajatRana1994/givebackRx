package com.app.givebackrx.appcode.settings.notificationModule


import android.os.Bundle
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
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.toast
import kotlinx.android.synthetic.main.fragment_notifications.*
import javax.inject.Inject


class NotificationsFragment : BaseFragment(), IChangePasswordView {

    @Inject
    lateinit var presenter: ChangePasswordPresenter<IChangePasswordView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)

        ivBack.setOnClickListener{requireActivity().onBackPressed()}
        if (isInternetConnected())
            presenter.notificationPreferenceGet()

        switchCoupon.setOnClickListener {
            if (isInternetConnected())
                presenter.notificationPreferenceSet(switchPromotion.isChecked.toString(),switchCoupon.isChecked.not().toString(),switchNews.isChecked.not().toString(),1)
        }
        switchNews.setOnClickListener{
            if (isInternetConnected())
                presenter.notificationPreferenceSet(switchPromotion.isChecked.toString(),switchCoupon.isChecked.toString(),switchNews.isChecked.not().toString(),2)
        }
        switchPromotion.setOnClickListener{
            if (isInternetConnected())
                presenter.notificationPreferenceSet(switchPromotion.isChecked.not().toString(),switchCoupon.isChecked.toString(),switchNews.isChecked.toString(),3)
        }
    }


    override fun onSecuritySuccess(it: SecurityDetailEntity) {

    }


    override fun OnNotificationResp(
        it: SuccessMessageEntity,
        type: Int
    ) {
        toast(it.message)
        if (type==1) switchCoupon.isChecked=!switchCoupon.isChecked
        if (type==2) switchNews.isChecked=!switchNews.isChecked
        if (type==3) switchPromotion.isChecked=!switchPromotion.isChecked
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token) {  }
        } catch (e: Exception) {
        }
    }

    override fun OnNotificationGetResp(it: NotificationEntity) {
        if (it.data.coupon_experience_satisfaction!=null) switchCoupon.isChecked=it.data.coupon_experience_satisfaction
        if (it.data.givebackrx_news!=null) switchNews.isChecked=it.data.givebackrx_news
        if (it.data.promotions!=null) switchPromotion.isChecked=it.data.promotions
    }

    override fun onUpdatedSuccess(it: SuccessMessageEntity) {
        toast(it.message)
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }


}
