package com.app.givebackrx.appcode.main.settings.referralsModule


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.app.givebackrx.R
import com.app.givebackrx.appcode.settings.referralsModule.IReferralView
import com.app.givebackrx.appcode.settings.referralsModule.ReferralAdapter
import com.app.givebackrx.appcode.settings.referralsModule.ReferralPresenter
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.DashboardEntity
import com.app.givebackrx.data.entity.ReferralEntity
import com.app.givebackrx.data.entity.ReferralItem
import com.app.givebackrx.data.entity.SignInWithUserDetailEntity
import com.app.givebackrx.listeners.SingleListCLickListener
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.*
import com.app.givebackrx.utils.spanBold
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_referrals.*
import java.lang.Exception
import javax.inject.Inject


class ReferralsFragment : BaseFragment(), IReferralView, SingleListCLickListener,
    AdapterView.OnItemSelectedListener {

    val mReferralList = mutableListOf<ReferralItem>()
    val mReferralAdapter by lazy { ReferralAdapter(mReferralList, this) }

    //    var count=0
    @Inject
    lateinit var presenter: ReferralPresenter<IReferralView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_referrals, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        rvRefferals.adapter = mReferralAdapter
        if (isInternetConnected())
            presenter.referral(spinnerReferrals.selectedItem.toString())

        ivBack.setOnClickListener { requireActivity().onBackPressed() }

        ivSearch.setOnClickListener {
            etSearch.onOffVisibility(etSearch.visibility == View.GONE)
            closeSearch.onOffVisibility(closeSearch.visibility == View.GONE)
        }
        spinnerReferrals.onItemSelectedListener = this
        etSearch.addWatcher {
            mReferralAdapter.filter.filter(it)
        }

        closeSearch.setOnClickListener {
            closeSearch.onOffVisibility(false)
            etSearch.onOffVisibility(false)
        }
        /*  rvRefferals.addOnScrollListener(object : RecyclerView.OnScrollListener() {
              override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                  if (count > 8)
                      if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == mNoticeList.size - 2) {
                          if (isInternetConnected()) presenter.notices(spinnserTasks.selectedItem.toString())
                      }
              }

              override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

              }
          })*/

        btnInvite.setOnClickListener { sendReferral() }
    }

    fun sendReferral() {
        val builder = AlertDialog.Builder(requireActivity())
        val viewDialog = this.layoutInflater.inflate(R.layout.referral_send_dialog, null)
        viewDialog.background= ContextCompat.getDrawable(
            requireActivity(),
            R.drawable.shadow_border_bg_more_rounded
        )
        builder.setView(viewDialog)
        val alertDialog = builder.create()
        val tvCode = viewDialog.findViewById<TextView>(R.id.tvCode)
        val tvdesc = viewDialog.findViewById<TextView>(R.id.tvdesc)
        val edtFirstName = viewDialog.findViewById<EditText>(R.id.edtFirstName)
        val edtLastName = viewDialog.findViewById<EditText>(R.id.edtLastName)
        val edtEmail = viewDialog.findViewById<EditText>(R.id.edtEmail)
        viewDialog.findViewById<ImageView>(R.id.close).setOnClickListener { alertDialog.dismiss() }

        tvCode.text = mPrefs.getKeyValue(PreferenceConstants.REFERRAL)
        tvdesc.text = SpannableString(tvdesc.text.toString()).apply {
            spanBold(4, 8)
        }

        viewDialog.findViewById<TextView>(R.id.btnSend).setOnClickListener {
            if (edtFirstName.text.isEmpty()) {
                toast("First name is required!", false)
            } else if (edtLastName.text.isEmpty()) {
                toast("Last name is required!", false)
            } else if (edtEmail.text.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString())
                    .matches().not()
            ) {
                toast("Valid email is required!", false)
            } else {
                if (alertDialog.getWindow()?.getCurrentFocus() != null) {
                    alertDialog.getWindow()!!.getCurrentFocus()!!.clearFocus();
                }
                alertDialog.dismiss()
                if (isInternetConnected()) {
                    JsonObject().apply {
                        addProperty("first_name", edtFirstName.text.toString())
                        addProperty("last_name", edtLastName.text.toString())
                        addProperty("type", "email")
                        addProperty("value", edtEmail.text.toString())
                        addProperty("user_type", mPrefs.getKeyValue(PreferenceConstants.USER_TYPE))
                        presenter.invitebyReferral(this)
                    }
                }
            }
        }

        alertDialog.setOnDismissListener {
            hideKeyboardDialog(alertDialog.currentFocus)
        }
        alertDialog.setCancelable(true)
        alertDialog.show()

        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window?.getAttributes())
        val dialogWindowWidth = (displayWidth * 0.98f).toInt()
        layoutParams.width = dialogWindowWidth
        alertDialog.window?.attributes = layoutParams
    }


    override fun onReferralResp(it: ReferralEntity) {

        tvTotalSent.text = it.data.total_referral.toString()
        tvTotalAccepted.text = it.data.total_accepted.toString()
        tvTotalPending.text = it.data.total_pending.toString()
        mReferralList.clear()
        mReferralList.addAll(it.data.referrals)
        mReferralAdapter.notifyDataSetChanged()
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token){}
        }catch (e: Exception){}
    }

    override fun onInvitedReferral(it: SignInWithUserDetailEntity) {
        toast(it.message)
    }

    override fun onResendReferralResp(it: DashboardEntity) {
        toast(it.message)
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

    override fun onSingleListClick(item: Any, position: Int) {
        if (isInternetConnected()) {
            presenter.resend(mReferralList[position].resend_button_id)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (isInternetConnected())
            presenter.referral(spinnerReferrals.selectedItem.toString())

    }


}
