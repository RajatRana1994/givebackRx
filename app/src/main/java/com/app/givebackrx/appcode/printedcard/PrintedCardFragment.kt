package com.app.givebackrx.appcode.printedcard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.givebackrx.R
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.MyCartEntity
import com.app.givebackrx.data.entity.PrintedCharity
import com.app.givebackrx.data.entity.PrintedCharityEntity
import com.app.givebackrx.localdb.LocalCartDataBs
import com.app.givebackrx.localdb.LocalCartEntity
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.*
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_printed_card.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class PrintedCardFragment : BaseFragment(), IPrintedCharityView, View.OnClickListener {
    val localCart by lazy { LocalCartDataBs.localCartDb(requireActivity()).localCartDao() }

    val cardList = mutableListOf<PrintedCharity>()
    val mCharityPrintedAdapter by lazy { CharityPrintedAdapter(requireActivity(), cardList) }
    var data: PrintedCharity? = null

    @Inject
    lateinit var presenter: PrintedCharityPresenter<IPrintedCharityView>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_printed_card, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        if (isInternetConnected())
            presenter.getCardPrintedCharity()
        parentLay.setOnTouchListener { view, motionEvent -> false }
        parentLayout.setOnTouchListener { view, motionEvent -> false }
        ivFlip.setOnClickListener(this)
        clFrontView.setOnClickListener(this)
        clBackView.setOnClickListener(this)
        spinnerCards.setOnClickListener(this)
        btnAddToCart.setOnClickListener(this)

    }


    override fun onClick(p0: View?) {
        when(p0!!){
            ivFlip->{  clFrontView.onOffCardVisibility(clBackView.visibility == View.VISIBLE)
                clBackView.onOffCardVisibility(clBackView.visibility == View.INVISIBLE)
            }
            clFrontView->{            if (data != null)
                cardPopup(
                        mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH)
                                .equals("gold"),
                        true,
                        data!!.pcn,
                        data!!.image_url ?: "",
                        data!!.member_id ?: "",
                        data!!.charity_name ?: "",
                        data!!.charity_name,
                        data!!.bin,
                        data!!.group,
                        true
                )
            }
            clBackView->{            if (data != null)
                cardPopup(
                        mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH)
                                .equals("gold"),
                        true,
                        data!!.pcn,
                        data!!.image_url ?: "",
                        data!!.member_id ?: "",
                        data!!.charity_name ?: "",
                        data!!.charity_name,
                        data!!.bin,
                        data!!.group,
                        false
                )
            }
            spinnerCards->charityDialog()
            btnAddToCart->{
                if (spinnerQuantity.selectedItemPosition>0)
                    addToCart(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
                else toast("Select Quantity",false)
            }
        }
    }


    override fun onPrintedCharityResp(it: PrintedCharityEntity) {
        cardList.clear()
        cardList.addAll(it.data.charities)
        if (it.data.charities.isNotEmpty()) {
            data = it.data.charities[0]
            mIvCardCharityImageFront.loadOrigImage(cardList[0].image_url)
            mTvPcn.text = cardList[0].pcn
            mTvMemberId.text = cardList[0].member_id
            mTvCardCharityName.text = cardList[0].charity_name
            mTvBin.text = cardList[0].bin
            mTvGroup.text = cardList[0].group
        }

        try {
            if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
                if (it.data.total_cart_items.isNullOrEmpty().not())
                    cartBedge.apply {
                        text = it.data.total_cart_items
                        onOffVisibility(it.data.total_cart_items.toInt() > 0)
                    }
                if (it.data.total_cart_items.toInt() > 0)
                    Intent("UPDATE_LIST").apply {
                        putExtra("cart_updated", true)
                        LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(this)
                    }
            } else getCartCount()

            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token) { }
        } catch (e: Exception) {
        }
    }

    fun getCartCount() {
        GlobalScope.launch {
            launch(Dispatchers.IO) {
                val alls = localCart.getAll()
                requireActivity().runOnUiThread {
                    cartBedge.apply {
                        text = alls.size.toString()
                        onOffVisibility(alls.size > 0)
                    }
                }
            }
        }
    }

    private fun addToCart(userLoggedIn: Boolean) {
        if (userLoggedIn) {
            if (isInternetConnected())
                JsonObject().apply {
                    addProperty("product_id", data!!.id)
                    addProperty("quantity", spinnerQuantity.selectedItem.toString())
                    addProperty("size", "")
                    presenter.addtoCart(this)
                }
        } else {
            addToCartLocally()
        }
    }

    private fun addToCartLocally() {
        GlobalScope.launch {
            launch(Dispatchers.IO) {
                val dbItems = localCart.getAll()
                var lastId = if (dbItems.isEmpty()) 0L else dbItems.get(dbItems.size - 1).id
                if (dbItems.size > 12) lastId = 0L
                if (lastId == 0L) {//mean db empty
                    localCart.insert(
                            LocalCartEntity(
                                    id = if (lastId == 0L) 0L else (lastId + 1).toLong(),
                                    name = data?.charity_name!!,
                                    price = "",
                                    imageLogo = data?.image_url!!,
                                    product_id = data?.id!!,
                                    quantity = spinnerQuantity.selectedItem.toString(),
                                    size = "",
                                    type = "charity"
                            )
                    )
                } else {
                    var getEntity: LocalCartEntity? = null
                    dbItems.forEachIndexed { index, entity ->
                        if (entity.product_id == data?.id!!) {
                            getEntity = entity
                        }
                    }
                    if (getEntity != null) {
                        localCart.update(
                                getEntity?.id!!,
                                getEntity?.quantity!!.toInt().plus(1).toString()
                        )
                    } else {
                        localCart.insert(
                                LocalCartEntity(
                                        id = if (lastId == 0L) 0L else (lastId + 1).toLong(),
                                        name = data?.charity_name!!,
                                        price = "",
                                        imageLogo = data?.image_url!!,
                                        product_id = data?.id!!,
                                        quantity = spinnerQuantity.selectedItem.toString(),
                                        size = "",
                                        type = "charity"
                                )
                        )
                    }
                }
                requireActivity().runOnUiThread {
                    toast("Item added to cart.", true)
                    getCartCount()
                    Intent("UPDATE_LIST").apply {
                        putExtra("cart_updated", true)
                        LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(this)
                    }
                }
            }
        }
    }

    override fun onAddToCartResp(it: MyCartEntity) {
        toast(it.message, true)
        if (it.total_cart_items.isNullOrEmpty().not()) {
            cartBedge.apply {
                text = it.total_cart_items
                onOffVisibility(it.total_cart_items.toInt() > 0)
            }

            if (it.total_cart_items.toInt() > 0)
                Intent("UPDATE_LIST").apply {
                    putExtra("cart_updated", true)
                    LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(this)
                }
        }
    }


    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }


    val listPopupWindow by lazy { ListPopupWindow(requireActivity()) }

    fun charityDialog() {
        listPopupWindow.setAdapter(mCharityPrintedAdapter)
        listPopupWindow.anchorView = spinnerCards
        listPopupWindow.isModal = false
        listPopupWindow.setBackgroundDrawable(
                ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.shape_rounded
                )
        )
        listPopupWindow.width = ListPopupWindow.WRAP_CONTENT
        listPopupWindow.height = cardList.size * 140
        listPopupWindow.animationStyle = android.R.style.Animation_Toast
        listPopupWindow.promptPosition - ListPopupWindow.POSITION_PROMPT_ABOVE
        listPopupWindow.verticalOffset = -24
        listPopupWindow.setOnItemClickListener { adapterView, view, i, l ->
            mIvCardCharityImageFront.loadOrigImage(cardList[i].image_url)
            mTvPcn.text = cardList[i].pcn
            mTvMemberId.text = cardList[i].member_id
            mTvCardCharityName.text = cardList[i].charity_name
            mTvBin.text = cardList[i].bin
            mTvGroup.text = cardList[i].group
            data = cardList[i]
            listPopupWindow.dismiss()
        }
        listPopupWindow.show()
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
                broardReceiver,
                IntentFilter("UPDATE_CART")
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(broardReceiver)
    }


    val broardReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1!!.hasExtra("cart_updated")) {
                if (p1.getBooleanExtra("cart_updated", true)) {
                    if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
                        if (isInternetConnected())
                            presenter.getCardPrintedCharity()
                        else{
                            getCartCount()
                        }
                }
            }
        }
    }

}