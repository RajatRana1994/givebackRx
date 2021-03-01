package com.app.givebackrx.appcode.payment

import android.app.Dialog
import android.os.Bundle
import android.text.InputFilter
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.app.givebackrx.R
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.*
import com.app.givebackrx.utils.extension.AllinOneDialog
import com.app.givebackrx.utils.extension.addWatcher
import com.app.givebackrx.utils.extension.onOffVisibility
import com.app.givebackrx.utils.extension.toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.row_saved_cards_cvv.view.*
import javax.inject.Inject


class PaymentFragment : BaseFragment(), IPaymentView {

    var coupon_info = ""
    val mSavedCard = mutableListOf<SavedCards>()

    @Inject
    lateinit var presenter: PaymentPresenter<IPaymentView>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }


    override fun setUp(view: View) {
        presenter.onAttach(this)
        val frag_value = arguments
        if (frag_value!!.containsKey("sixforone")) {
            etCoupon.setText("6FOR1")
            if (isInternetConnected()) {
                presenter.goldCardPaymentSummary("6FOR1")
            }
        }

        mBtnApply.setOnClickListener {
            if (isInternetConnected()) {
                presenter.goldCardPaymentSummary(etCoupon.text.toString().trim())
            }
        }

        mIvInfo.setOnClickListener { showInformationPop("Coupon Description", coupon_info) }



        if (isInternetConnected()) {
            presenter.goldCardPaymentSummaryDetail("")
        }
        ivBack.setOnClickListener { requireActivity().onBackPressed() }
        btnPay.setOnClickListener { checkPaymentThrough() }
        etCardNumber.addWatcher {
            cardTypeName.text = if (CardValidator.validateCardNumber(etCardNumber.text.toString()))
                (CardValidator.getCardType(etCardNumber.text.toString()).name) else ""
            cardTypeName.onOffVisibility(cardTypeName.text.isNotEmpty())
        }
        applyFilters()
    }

    fun checkPaymentThrough() {
        if (mSavedCard.isEmpty()) {
            goldPaymentWithDetail()
        } else {
            var cardindex = -1
            savedCardInflater.children.forEachIndexed { index, view ->
                if (view.rbBtn.isChecked) {
                    cardindex = index
                }
            }
            if (cardindex > -1) {
                if (savedCardInflater.getChildAt(cardindex).etCVV.text.isEmpty() || savedCardInflater.getChildAt(
                                cardindex
                        ).etCVV.text.length < 3
                ) toast("Enter correct CVV", false)
                else if (savedCardInflater.getChildAt(cardindex).etExpiry.text.contains("/")) {
                    if (CardValidator.validateExpiryDate(
                                    savedCardInflater.getChildAt(cardindex).etExpiry.text.toString()
                                            .substringBefore("/"),
                                    savedCardInflater.getChildAt(cardindex).etExpiry.text.toString()
                                            .substringAfter("/")
                            )
                    ) {
                        JsonObject().apply {
                            addProperty("card_id", mSavedCard[cardindex].card_id)
                            addProperty("expire_month", etExpiry.text.toString().substringBefore("/").replace("/", ""))
                            addProperty("expire_year", etExpiry.text.toString().substringAfter("/").replace("/", ""))
                            addProperty("cvv", etCVV.text.toString())
                            presenter.goldCardPayment(this)
                        }
                    } else toast("Invalid card Expiry", false)
                } else toast("Enter card Expiry", false)
            } else {
                goldPaymentWithDetail()
            }
        }
    }

    fun goldPaymentWithDetail() {
        if (etFirstname.text.isEmpty()) toast("Enter First Name", false)
        else if (etLastname.text.isEmpty()) toast("Enter Last Name", false)
        else if (!CardValidator.validateCardNumber(etCardNumber.text.toString())) toast(
                "Invalid Card Number",
                false
        )
        else if (etCVV.text.isEmpty() && etCVV.text.toString().length < 3) toast(
                "Enter correct CVV",
                false
        )
        else {
            if (etExpiry.text.contains("/")) {
                if (CardValidator.validateExpiryDate(
                                etExpiry.text.toString().substringBefore("/"),
                                etExpiry.text.toString().substringAfter("/")
                        )
                ) {
                    JsonObject().apply {
                        addProperty("first_name", etFirstname.text.toString())
                        addProperty("last_name", etLastname.text.toString())
                        addProperty("card_number", CardValidator.sanitizeEntry(etCardNumber.text.toString(), true))
                        addProperty("expire_month", etExpiry.text.toString().substringBefore("/").replace("/", ""))
                        addProperty("expire_year", etExpiry.text.toString().substringAfter("/").replace("/", ""))
                        addProperty("cvv", etCVV.text.toString())
                        addProperty("is_save_card", (cbSaveCardFuture.isChecked).toString())
                        addProperty("payment_type", if (cbSaveCardRecurring.isChecked) "recurringPayment" else "oneTimePayment")
                        addProperty("card_type", CardValidator.getCardType(etCardNumber.text.toString()).name)
                        addProperty("coupon_type", "membership")
                        addProperty("coupon", etCoupon.text.toString().trim())
                        presenter.goldCardPayment(this)
                    }
                } else toast("Invalid card Expiry", false)
            } else toast("Invalid card Expiry", false)
        }
    }

    override fun goldCardPaymentSummary(it: OrderSummaryModel) {
        toast(it.message)
        tvSubtotal.setText(it.data.order_summary.subtotal)
        tvPriceTagDiscount.setText(it.data.order_summary.discount)
        tvTotal.setText(it.data.order_summary.final_amount)
        if (it.data.order_summary.coupon_description!!.isNotEmpty()) {
            mIvInfo.visibility = View.VISIBLE
            coupon_info = it.data.order_summary.coupon_description.toString()
        } else {
            mIvInfo.visibility = View.GONE
            coupon_info = ""
        }

    }

    override fun onMembershipResp(it: MemberShipOrderEntity) {
        tvsavedCard.onOffVisibility(it.data.saved_cards.isNotEmpty())
        mSavedCard.addAll(it.data.saved_cards)
        it.data.saved_cards.forEachIndexed { index, savedCard ->
            addSavedCards(savedCard, index)
        }
    }

    override fun goldCardPaymentSummaryError(it: OrderSummaryModel) {
        toast(it.message)
        coupon_info=""
        mIvInfo.visibility=View.GONE
    }


    override fun onPaymentResp(it: PaymentEntity) {
        toast(it.message, it.success)
//        try {
//            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
//                decodeToken(it.auth.new_jwt_token) {
//
//                }
//        } catch (e: Exception) {
//        }
        requireActivity().onBackPressed()
    }

    override fun onSavedCardsResp(it: CartEntity) {
//        tvsavedCard.onOffVisibility(it.data.saved_cards.isNotEmpty())
//        mSavedCard.addAll(it.data.saved_cards)
//        it.data.saved_cards.forEachIndexed { index, savedCard ->
//            addSavedCards(savedCard, index)
//        }
    }

    private fun addSavedCards(card: SavedCards, index: Int) {
        layoutInflater.inflate(R.layout.row_saved_cards_cvv, savedCardInflater as ViewGroup, false)
                .apply {
                    findViewById<ImageView>(R.id.ivDelete).setOnClickListener {
                        AllinOneDialog("",
                                if (card.recurring_payment) "This card is added for recurring payments of Gold membership. Deleting this card will stop recurring payments. Would you still like to delete this card?"
                                else "Would you still like to delete this card?",
                                "No", "Yes", {}, {
                            if (isInternetConnected())
                                presenter.deleteSavedCard(card.card_id, index)
                        })
                    }
                    findViewById<TextView>(R.id.mCardNum).text = card.card_number
                    findViewById<TextView>(R.id.cardTypeName).text = card.card_type
                    findViewById<CheckedTextView>(R.id.rbBtn).setOnClickListener {
                        savedCardInflater.children.forEachIndexed { indx, view ->
                            if (indx != index) {
                                it.tvExpiry.onOffVisibility(false)
                                it.etExpiry.onOffVisibility(false)
                                it.etCVV.onOffVisibility(false)
                                it.tvCVV.onOffVisibility(false)
                                it.rbBtn.isChecked = (false)
                            }
                        }
                        savedCardInflater.getChildAt(index).rbBtn.isChecked =
                                savedCardInflater.getChildAt(index).rbBtn.isChecked.not()
                        findViewById<EditText>(R.id.etCVV).onOffVisibility(
                                savedCardInflater.getChildAt(
                                        index
                                ).rbBtn.isChecked
                        )
                        findViewById<TextView>(R.id.tvCVV).onOffVisibility(
                                savedCardInflater.getChildAt(
                                        index
                                ).rbBtn.isChecked
                        )
                        findViewById<TextView>(R.id.tvExpiry).onOffVisibility(
                                savedCardInflater.getChildAt(
                                        index
                                ).rbBtn.isChecked
                        )
                        findViewById<EditText>(R.id.etExpiry).onOffVisibility(
                                savedCardInflater.getChildAt(
                                        index
                                ).rbBtn.isChecked
                        )

                    }
                    savedCardInflater.addView(this)
                }
    }

    override fun onDeletedCardResp(it: FavoriteMedicationEntity, position: Int) {
        savedCardInflater.removeViewAt(position)
    }


    private fun applyFilters() {
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
    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

    val mInfoBuilder: Dialog by lazy { Dialog(requireActivity()) }

    fun showInformationPop(ttl: String = "", desc: String) {
        mInfoBuilder.setContentView(R.layout.charitysignee_info_dialog);
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        mInfoBuilder.window!!.setBackgroundDrawableResource(R.drawable.shadow_border_bg_more_rounded)
        mInfoBuilder.window!!.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
                (displayMetrics.widthPixels * 0.99).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mInfoBuilder.findViewById<TextView>(R.id.titleHeader).isAllCaps = false
        mInfoBuilder.findViewById<TextView>(R.id.titleHeader).apply {
            text = ttl
        }
        mInfoBuilder.findViewById<TextView>(R.id.tvDesc).text = desc

        mInfoBuilder.findViewById<TextView>(R.id.tvDone)
                .setOnClickListener {
                    mInfoBuilder.dismiss()
                }
        mInfoBuilder.show();
    }

}