package com.app.givebackrx.appcode.storecheckout

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.givebackrx.R
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.payment.CardValidator
import com.app.givebackrx.appcode.selectaddress.SelectAddressFragment
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.CartEntity
import com.app.givebackrx.data.entity.FavoriteMedicationEntity
import com.app.givebackrx.data.entity.SavedCard
import com.app.givebackrx.data.entity.SuccessMessageEntity
import com.app.givebackrx.utils.extension.*
import kotlinx.android.synthetic.main.fragment_store_checkout.*
import kotlinx.android.synthetic.main.row_saved_cards_cvv.view.*
import javax.inject.Inject

class StoreCheckoutFragment : BaseFragment(), IStoreCheckoutView {
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }

    @Inject
    lateinit var presenter: StoreCheckoutPresenter<IStoreCheckoutView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store_checkout, container, false)
    }


    override fun setUp(view: View) {
        presenter.onAttach(this)
        btnSelectAddress.setOnClickListener {
            mainActivity.addMainFragment(
                R.id.homeContainer,
                SelectAddressFragment()
            )
        }
        setCartData()
        paymentViewinit()
        btnPay.setOnClickListener {
            checkPaymentThrough()
        }
//        addSavedCards(it)
    }

    private fun paymentViewinit() {
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
    }

    private fun setCartData() {
        savedCardLayout.onOffVisibility(storeData!!.data.saved_cards.size > 0)
        storeData!!.data.saved_cards.forEachIndexed { index, savedCard ->
            addSavedCards(
                savedCard,
                index
            )
        }
        tvSubtotal.text = storeData!!.data.order_summary.subtotal
        tvShipping.text = storeData!!.data.order_summary.shipping
        tvEstimatedTax.text = storeData!!.data.order_summary.estimated_tax
        tvPriceTagDiscount.text = storeData!!.data.order_summary.discount
        tvTotal.text = storeData!!.data.order_summary.final_amount
        tvName.text = storeData!!.data.default_address.address_type
        tvAddress.text =
            "${storeData!!.data.default_address.apt_suit_number}, ${storeData!!.data.default_address.address}"
        tvPhone.text = storeData!!.data.default_address.phone

    }

    fun checkPaymentThrough() {
        if (storeData!!.data.saved_cards.isEmpty()) {
            paymentWithDetail()
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
                        paymentWithSavedCard(
                            storeData!!.data.saved_cards[cardindex],
                            savedCardInflater.getChildAt(cardindex).etCVV.text.toString()
                        )
                    } else toast("Invalid card Expiry", false)
                } else toast("Enter card Expiry", false)
//                else paymentWithSavedCard(storeData!!.data.saved_cards[cardindex],savedCardInflater.getChildAt(cardindex).etCVV.text.toString())
            } else {
                paymentWithDetail()
            }
        }
    }

    /*first_name:Shruti
    last_name:Rathore
    card_number:4111111111111111
    expire_month:10
    expire_year:2020
    cvv:3242
    amount:39.00
    catalog_order_id:a0fW0000002VSheIAG
    is_save_card:true
    card_type:Visa*/
    fun paymentWithSavedCard(
        savedCard: SavedCard,
        cvv: String
    ) {
        hashMapOf<String, Any>().apply {
            put("first_name", savedCard.first_name.toString())
            put("last_name", savedCard.last_name.toString())
            put("card_number", savedCard.card_number)
            put("expire_month", savedCard.expiry_month)
            put("expire_year", savedCard.expiry_year)
            put("cvv", cvv)//
            put("amount", storeData!!.data.order_summary.final_amount)
            put("catalog_order_id", storeData!!.data.catalog_order_id)
            put("is_save_card", true)
            put("card_type", savedCard.card_type)
            if (isInternetConnected()) presenter.paymentCart(this)
        }

    }

    fun paymentWithDetail() {
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
                    hashMapOf<String, Any>().apply {
                        put("first_name", etFirstname.text.toString())
                        put("last_name", etLastname.text.toString())
                        put(
                            "card_number",
                            CardValidator.sanitizeEntry(etCardNumber.text.toString(), true)
                        )
                        put(
                            "expire_month",
                            etExpiry.text.toString().substringBefore("/").replace("/", "")
                        )
                        put(
                            "expire_year",
                            etExpiry.text.toString().substringAfter("/").replace("/", "")
                        )
                        put("cvv", etCVV.text.toString())
                        put("amount", storeData!!.data.order_summary.final_amount.replace("$", ""))
                        put("catalog_order_id", storeData!!.data.catalog_order_id)
                        put("is_save_card", cbSaveCardFuture.isChecked.toString())
                        put(
                            "card_type",
                            CardValidator.getCardType(etCardNumber.text.toString()).name
                        )
                        if (isInternetConnected()) presenter.paymentCart(this)
                    }
                } else toast("Invalid card Expiry", false)
            } else toast("Invalid card Expiry", false)
        }

    }

    private fun addSavedCards(card: SavedCard, index: Int) {
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
                    findViewById<TextView>(R.id.tvCVV).onOffVisibility(
                        savedCardInflater.getChildAt(
                            index
                        ).rbBtn.isChecked
                    )
                    findViewById<EditText>(R.id.etCVV).onOffVisibility(
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

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

    override fun onCartPaymentResp(it: SuccessMessageEntity) {
        toast(it.message, true)
        Handler().postDelayed({ requireActivity().onBackPressed() }, 800)
    }

    companion object {
        var storeData: CartEntity? = null
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
            broardReceiver,
            IntentFilter("ADDRESS_CHANGED")
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(broardReceiver)
    }

    val broardReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1!!.hasExtra("Changed")) {
                if (p1.getStringExtra("Changed").equals("true")) {
                    setCartData()
                }
            }
        }
    }
}