package com.app.givebackrx.appcode.main.settings.updateCardModule


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.app.givebackrx.R
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.payment.CardValidator
import com.app.givebackrx.appcode.payment.PaymentFragment
import com.app.givebackrx.appcode.settings.updateCardModule.IUpdateCardView
import com.app.givebackrx.appcode.settings.updateCardModule.SavedCardsAdapter
import com.app.givebackrx.appcode.settings.updateCardModule.UpdateCardPresenter
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.*
import com.app.givebackrx.listeners.SingleListCLickListener
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_update_card.*
import java.util.*
import javax.inject.Inject


class UpdateCardFragment : BaseFragment(), IUpdateCardView {

    @Inject
    lateinit var presenter: UpdateCardPresenter<IUpdateCardView>
    var data: DashboardData? = null
    var alertDialog: AlertDialog? = null

    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity)
            mainActivity = context
    }

    val mSavedCard = mutableListOf<SavedCards>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_card, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        if (isInternetConnected())
            presenter.dashboard()
        ivBack.setOnClickListener { requireActivity().onBackPressed() }
        ivFlip.setOnClickListener {
            clFrontView.onOffCardVisibility(clBackView.visibility == View.VISIBLE)
            clBackView.onOffCardVisibility(clBackView.visibility == View.INVISIBLE)
        }
        updateAccordingPref()
        btnUpgrade.setOnClickListener {
            upgradeCard {
                val args = Bundle()
                args.putString("from_dashboard", "no")
                val paymentFragment = PaymentFragment()
                paymentFragment.setArguments(args)
                mainActivity.addMainFragment(R.id.homeContainer, paymentFragment)
            }
        }

        clFrontView.setOnClickListener {
            if (data != null)
                cardPopup(
                    mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH)
                        .equals("gold"),
                    false,
                    data!!.card_detail.pcn,
                    data!!.charity_logo ?: "",
                    data!!.card_detail.member_id ?: "",
                    data!!.card_detail.member_name ?: "",
                    data!!.charity!!.charity_name!!,
                    data!!.card_detail.bin,
                    data!!.card_detail.group,
                    true
                )
        }
        clBackView.setOnClickListener {
            if (data != null)
                cardPopup(
                    mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH)
                        .equals("gold"),
                    false,
                    data!!.card_detail.pcn,
                    data!!.charity_logo ?: "",
                    data!!.card_detail.member_id ?: "",
                    data!!.card_detail.member_name ?: "",
                    data!!.charity!!.charity_name!!,
                    data!!.card_detail.bin,
                    data!!.card_detail.group,
                    false
                )
        }

        mTvEmail.setOnClickListener {
            alertDialog = sendEmail(
                getString(R.string.enter_email_to_send_card),
                getString(
                    R.string.terms_silver,
                    if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
                            .isEmpty()
                    ) "silver" else mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
                        .toLowerCase(Locale.ENGLISH)
                )
            ) {
                if (isInternetConnected()) {
                    hideKeyboardDialog(alertDialog!!.window!!.decorView)
                    with(JsonObject()) {
                        addProperty("email", it)
                        addProperty("charity_id", "")
                        addProperty("card_type", "user")
                        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
                            addProperty(
                                "membership_type",
                                mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
                            )
                        presenter.emailCard(this)
                    }
                }
            }
        }
        mTvPhone.setOnClickListener {
            alertDialog = sendEmail(
                getString(R.string.enter_phone_no_to_send_card), titl = "Phone number",
                terms = getString(
                    R.string.terms_silver,
                    if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
                            .isEmpty()
                    ) "silver" else mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
                        .toLowerCase(Locale.ENGLISH)
                )
            ) {
                if (isInternetConnected()) {
                    hideKeyboardDialog(alertDialog!!.window!!.decorView)
                    with(JsonObject()) {
                        addProperty("phone", it.replace(Regex("[()-]"), "").replace(" ", ""))
//                        addProperty("charity_id", "")
                        addProperty("card_type", "user")
                        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
                            addProperty(
                                "membership_type",
                                mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
                            )
                        presenter.textCard(this)
                    }
                }
            }
        }
        btnUpdateForRecurring.setOnClickListener {
            updateCards {firstname,lastname,cardnum,expiryMonth,expiryYear,etCVV,cardType->
                presenter.updateCards(firstname, lastname, cardnum, expiryMonth, expiryYear, etCVV, cardType)
            }
        }
        btnDowngrade.setOnClickListener {
            askDownGradePlan {
                downGradePlan {
                    presenter.disableRecurringPayment(it)
                }
            }
        }
    }

    fun updateAccordingPref() {
        if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH).equals("gold")) {

            presenter.goldCardPaymentSummary("")

            clFrontView.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.gold_card_gradient_bg)
            clBackView.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.gold_card_gradient_bg)
            btnUpgrade.onOffVisibility(false)
        }else{
            btnUpgrade.onOffVisibility(true)
        }

        btnUpdateForRecurring.onOffVisibility(
            mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH)
                .equals("gold")
        )
    }

    override fun onDashboardResp(it: DashboardEntity) {
        parentLayout.onOffVisibility(true)
        data = it.data
        mTvPcn.text = it.data.card_detail.pcn
        mTvMemberId.text = it.data.card_detail.member_id
        mTvMemberName.text = it.data.card_detail.member_name ?: ""
        mTvCardCharityName.text = it.data.charity!!.charity_name
        mTvBin.text = it.data.card_detail.bin
        mTvGroup.text = it.data.card_detail.group
        mIvCardCharityImageFront.loadOrigImage(it.data.charity_logo)
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser")) decodeToken(it.auth.new_jwt_token) {
                updateAccordingPref()

            }
        } catch (e: Exception) {
        }
    }

    override fun onMembershipResp(it: MemberShipOrderEntity) {
        btnDowngrade.onOffVisibility(it.data.is_recurring_payment_active)
        if (it.data.saved_cards.isNotEmpty()) {
            mTvSavedCard.onOffVisibility(true)
            rvSavedCard.onOffVisibility(true)
            mSavedCard.apply {
                clear()
                addAll(it.data.saved_cards)
                rvSavedCard.adapter = SavedCardsAdapter(this, object : SingleListCLickListener {
                    override fun onSingleListClick(item: Any, position: Int) {
                        AllinOneDialog("",
                                if (this@apply[position].recurring_payment) getString(R.string.recurring_message)
                                else getString(R.string.still_delete),
                                getString(R.string.no), getString(R.string.yes), {}, {
                            if (isInternetConnected())
                                presenter.deleteSavedCard(this@apply[position].card_id, position)
                        })
                    }
                })
            }
        }
    }

    override fun onCardViaEmailed(it: SuccessMessageEntity) {
        if (alertDialog != null) {
            if (alertDialog!!.isShowing) alertDialog!!.dismiss()
        }
        hideAllTypeKB(requireView().rootView.windowToken)
        toast(it.message, true)
    }

    override fun onSavedCardsResp(it: CartEntity) {
//        btnDowngrade.onOffVisibility(it.data.is_recurring_payment_active)
//        if (it.data.saved_cards.isNotEmpty()) {
//            mTvSavedCard.onOffVisibility(true)
//            rvSavedCard.onOffVisibility(true)
//            mSavedCard.apply {
//                clear()
//                addAll(it.data.saved_cards)
//                rvSavedCard.adapter = SavedCardsAdapter(this, object : SingleListCLickListener {
//                    override fun onSingleListClick(item: Any, position: Int) {
//                        AllinOneDialog("",
//                            if (this@apply[position].recurring_payment) "This card is added for recurring payments of Gold membership. Deleting this card will stop recurring payments. Would you still like to delete this card?"
//                            else "Would you still like to delete this card?",
//                            "No", "Yes", {}, {
//                                if (isInternetConnected())
//                                presenter.deleteSavedCard(this@apply[position].card_id, position)
//                            })
//                    }
//                })
//            }
//        }
    }

    override fun onDeletedCardResp(it: FavoriteMedicationEntity, position: Int) {
        toast(it.message)
        mSavedCard.removeAt(position)
        rvSavedCard.adapter!!.notifyItemRemoved(position)
        mTvSavedCard.onOffVisibility(mSavedCard.isNotEmpty())
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }


    public fun askDownGradePlan(onClick: () -> Unit) {
        val builder = AlertDialog.Builder(requireActivity())
        val viewDialog = this.layoutInflater.inflate(R.layout.dialog_ask_downgrade_plan, null)
        viewDialog.background = ContextCompat.getDrawable(
            requireActivity(),
            R.drawable.shadow_border_bg_more_rounded
        )
        builder.setView(viewDialog)
        val alertDialog = builder.create()
        viewDialog.findViewById<ImageView>(R.id.ivClose).setOnClickListener { alertDialog.dismiss() }

        viewDialog.findViewById<TextView>(R.id.btnKeepPlan).apply {
            setOnClickListener {
                alertDialog.dismiss()
            }
        }
        viewDialog.findViewById<TextView>(R.id.btnDowngrade).apply {
            setOnClickListener {
                alertDialog.dismiss()
                onClick()
            }
        }
        alertDialog.setCancelable(true)
        alertDialog.show()

        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window?.getAttributes())
        val dialogWindowWidth = (displayWidth * 0.92f).toInt()
        layoutParams.width = dialogWindowWidth
        alertDialog.window?.attributes = layoutParams
    }

    public fun downGradePlan(onClick: (String) -> Unit) {
        val builder = AlertDialog.Builder(requireActivity())
        val viewDialog = this.layoutInflater.inflate(R.layout.dialog_downgrade_plan, null)
        viewDialog.background = ContextCompat.getDrawable(
            requireActivity(),
            R.drawable.shadow_border_bg_more_rounded
        )
        builder.setView(viewDialog)
        val alertDialog = builder.create()
        viewDialog.findViewById<ImageView>(R.id.ivClose).setOnClickListener { alertDialog.dismiss() }


        viewDialog.findViewById<TextView>(R.id.btnDowngrade).apply {
            setOnClickListener {
                hideAllTypeKB(viewDialog.windowToken)
                if (viewDialog.findViewById<EditText>(R.id.edtFeedbackForDowngrade).text.isEmpty()) {
                    toast(context.getString(R.string.pleaseenterfeedback), false)
                } else {
                    alertDialog.dismiss()
                    onClick(viewDialog.findViewById<EditText>(R.id.edtFeedbackForDowngrade).text.toString())
                }
            }
        }
        alertDialog.setCancelable(true)
        alertDialog.show()

        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window?.getAttributes())
        val dialogWindowWidth = (displayWidth * 0.9f).toInt()
        layoutParams.width = dialogWindowWidth
        alertDialog.window?.attributes = layoutParams
    }


    public fun updateCards(onClick: (String,String,String,String,String,String,String) -> Unit) {
        val builder = AlertDialog.Builder(requireActivity())
        val viewDialog = this.layoutInflater.inflate(R.layout.dialog_update_cards, null)
        viewDialog.background = ContextCompat.getDrawable(
            requireActivity(),
            R.drawable.shadow_border_bg_more_rounded
        )
        builder.setView(viewDialog)
        val alertDialog = builder.create()

        viewDialog.findViewById<ImageView>(R.id.ivClose).setOnClickListener { alertDialog.dismiss() }
        val etFirstname = viewDialog.findViewById<EditText>(R.id.etFirstname)
        val etLastname = viewDialog.findViewById<EditText>(R.id.etLastname)
        val etCardNumber = viewDialog.findViewById<EditText>(R.id.etCardNumber)
        val cardTypeName = viewDialog.findViewById<TextView>(R.id.cardTypeName)
        val etExpiry = viewDialog.findViewById<EditText>(R.id.etExpiry)
        val etCVV = viewDialog.findViewById<EditText>(R.id.etCVV)
        etCardNumber.addWatcher {
            cardTypeName.text = if (CardValidator.validateCardNumber(etCardNumber.text.toString()))
                (CardValidator.getCardType(etCardNumber.text.toString()).name) else ""
            cardTypeName.onOffVisibility(cardTypeName.text.isNotEmpty())
        }

        etCardNumber.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            if (source.length > 0) {
                if (!Character.isDigit(source[0])) return@InputFilter "" else {
                    if (dstart == 4) {
                        return@InputFilter " $source"
                    } else if (dstart == 9) {
                        return@InputFilter " $source"
                    } else if (dstart == 14) return@InputFilter " $source"
                    else if (dstart > 18) return@InputFilter ""
                    else return@InputFilter "$source"
                }
            }
            null
        })
        etExpiry.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            if (source.length > 0) {
                if (!Character.isDigit(source[0])) {
                    return@InputFilter ""
                } else if (dstart == 1)
                    return@InputFilter "$source/"
                else if (dstart > 6)
                    return@InputFilter ""
                else return@InputFilter "$source"
            }
            null
        })

        viewDialog.findViewById<Button>(R.id.btnUpdate).apply {
            setOnClickListener {
                hideAllTypeKB(viewDialog.windowToken)

                if (etFirstname.text.isEmpty()) toast(context.getString(R.string.enterfirstname), false)
                else if (etLastname.text.isEmpty()) toast(context.getString(R.string.enterlastname), false)
                else if (!CardValidator.validateCardNumber(etCardNumber.text.toString())) toast(
                    context.getString(R.string.invalidcardnumber),
                    false
                )
                else if (etCVV.text.isEmpty() && etCVV.text.toString().length < 3) toast(
                    context.getString(R.string.entercorrectno),
                    false
                )
                else {

                    if (etExpiry.text.contains("/")) {
                        if (CardValidator.validateExpiryDate(
                                etExpiry.text.toString().substringBefore("/"),
                                etExpiry.text.toString().substringAfter("/")
                            )
                        ) {
                            alertDialog.dismiss()
                            onClick(
                                etFirstname.text.toString(),
                                etLastname.text.toString(),
                                CardValidator.sanitizeEntry(etCardNumber.text.toString(), true),
                                etExpiry.text.toString().substringBefore("/").replace("/", ""),
                                etExpiry.text.toString().substringAfter("/").replace("/", ""),
                                etCVV.text.toString(),
                                CardValidator.getCardType(etCardNumber.text.toString()).name
                            )
                        } else toast(context.getString(R.string.invalid_card_expiry), false)
                    } else toast(context.getString(R.string.invalid_card_expiry), false)
                }
            }
        }
        alertDialog.setCancelable(true)
        alertDialog.show()

        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window?.getAttributes())
        val dialogWindowWidth = (displayWidth * 0.9f).toInt()
        layoutParams.width = dialogWindowWidth
        alertDialog.window?.attributes = layoutParams
    }

    override fun onDisableRecurringPaymentResp(it: SuccessMessageEntity) {
        AllinOneDialog("", it.message, "", "Ok", {
            presenter.goldCardPaymentSummary("")
        }, {
            presenter.goldCardPaymentSummary("")
        })
    }

    override fun onUpdateCardResp(it: SuccessMessageEntity) {
        toast(it.message,true)
        presenter.goldCardPaymentSummary("")
    }

}
