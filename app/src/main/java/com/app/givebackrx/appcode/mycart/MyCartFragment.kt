package com.app.givebackrx.appcode.mycart

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.appcode.loginEmailPaswordModule.LoginEmailPasswordActivity
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.appcode.orderSummary.shippingModule.ShippingFragment
import com.app.givebackrx.appcode.storecheckout.StoreCheckoutFragment
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.CartEntity
import com.app.givebackrx.data.entity.CartItem
import com.app.givebackrx.data.entity.MyCartEntity
import com.app.givebackrx.localdb.LocalCartDataBs
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.AllinOneDialog
import com.app.givebackrx.utils.extension.addMainFragment
import com.app.givebackrx.utils.extension.toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_my_cart.*
import kotlinx.android.synthetic.main.fragment_my_cart.etCoupon
import kotlinx.android.synthetic.main.fragment_my_cart.mBtnApply
import kotlinx.android.synthetic.main.fragment_my_cart.mIvInfo
import kotlinx.android.synthetic.main.fragment_my_cart.tvEstimatedTax
import kotlinx.android.synthetic.main.fragment_my_cart.tvPriceTagDiscount
import kotlinx.android.synthetic.main.fragment_my_cart.tvShipping
import kotlinx.android.synthetic.main.fragment_my_cart.tvSubtotal
import kotlinx.android.synthetic.main.fragment_my_cart.tvTotal
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

class
MyCartFragment : BaseFragment(), IMyCartView, (String, Int) -> Unit, View.OnClickListener {

    var couponDescription=""
    val localCart by lazy { LocalCartDataBs.localCartDb(requireActivity()).localCartDao() }
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mainActivity = context
    }

    val mCartItems = mutableListOf<CartItem>()
    val mMyCartAdapter by lazy { MyCartAdapter(mCartItems, this) }
    var mCartEntity: CartEntity? = null

    @Inject
    lateinit var presenter: MyCartPresenter<IMyCartView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_cart, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        rvCartItemList.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                RecyclerView.VERTICAL
            )
        )
        btnCheckout.setOnClickListener(this)
        rvCartItemList.adapter = mMyCartAdapter
//        presenter.
        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN).not()) {
            getCartItem()
        } else {
            if (isInternetConnected()) checkToUpdateServer()
        }

        mBtnApply.setOnClickListener {
            if (etCoupon.text.toString().isNotEmpty()){
                if (isInternetConnected()) checkToUpdateServer()
            }
        }

        mIvInfo.setOnClickListener { showInformationPop("Coupon Description", couponDescription) }
    }

    private fun checkToUpdateServer() {
        GlobalScope.launch {
            launch(Dispatchers.IO) {
                val alls = localCart.getAll()
                if (alls.isEmpty()) {
                    requireActivity().runOnUiThread {
                        presenter.getCart(JsonObject().apply {
                            addProperty("coupon", etCoupon.text.toString().trim())
                            add("product_array", JsonArray())
                        })
                    }
                } else {
                    JsonObject().apply {addProperty("coupon", etCoupon.text.toString().trim())
                        add("product_array", JsonArray().apply {
                                alls.forEachIndexed { index, localCartEntity ->
                                    add(JsonObject().apply {
                                    addProperty("product_id", localCartEntity.product_id)
                                    addProperty("quantity", localCartEntity.quantity)
                                })
                            }
                        })
                        requireActivity().runOnUiThread {
                            if (isInternetConnected())
                                presenter.getCart(this)
                        }
                    }
                }
            }
        }
    }

    fun getCartItem() {
        GlobalScope.launch {
            launch(Dispatchers.IO) {
                localCart.getAll().forEachIndexed { index, entity ->
                    mCartItems.add(
                        CartItem(
                            discounted_price = entity.price,
                            display_product_name = entity.name,
                            product_id = entity.product_id,
                            quantity = entity.quantity,
                            size = entity.size,
                            image_url = entity.imageLogo,
                            sizes = arrayListOf<String>()
                        )
                    )
                }
                requireActivity().runOnUiThread {
                    mMyCartAdapter.notifyDataSetChanged()
                    if (mCartItems.isEmpty()){
                        lblSimilarItem.visibility=View.GONE
                        clCoupon.visibility=View.GONE
                        checkoutLayout.visibility=View.GONE
                    }else{
                        lblSimilarItem.visibility=View.VISIBLE
                        clCoupon.visibility=View.VISIBLE
                        checkoutLayout.visibility=View.VISIBLE
                    }
//                    var total = 0.00
//                    mCartItems.forEach {
//                        if (it.discounted_price.isNotEmpty())
//                            total = total.plus(it.discounted_price.replace("$", "").toDouble())
//                    }
//                    tvSubtotal.text = "$$total"
//                    tvTotal.text = "$$total"


                    var total = 0.00
                    mCartItems.forEach {
                        if (it.discounted_price.isNotEmpty()){
                            var quantity=it.quantity
                            var totalAmount=it.discounted_price.replace("$", "").toDouble() * quantity.toInt()
                            total = total.plus(totalAmount)
                        }

                    }
                    if (!mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
                        var total_new=roundOffDecimal(total)
                        tvSubtotal.text = "$$total_new"
                        tvTotal.text = "$$total_new"
                    }else{
                        tvSubtotal.text = "$$total"
                        tvTotal.text = "$$total"
                    }
                }
            }
        }
    }

    fun updateCartItem(pos: Int, qty: String, action: String) {
        GlobalScope.launch {
            launch(Dispatchers.IO) {
                val alls = localCart.getAll()
                localCart.update(alls[pos].id, qty)
                requireActivity().runOnUiThread {
                    toast(
                        "Item quantity ${if (action.equals("plus")) "increased" else "decreased"} successfully!!",
                        true
                    )
                }
            }
        }
    }

    fun deleteItem(pos: Int, deleted: () -> Unit) {
        GlobalScope.launch {
            launch(Dispatchers.IO) {
                try {
                    localCart.delete(localCart.getAll()[pos])
                    deleted.invoke()
                    requireActivity().runOnUiThread {
                        Intent("UPDATE_CART").apply {
                            putExtra("cart_updated", true)
                            LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(this)
                        }
                    }
                } catch (e: Exception) {
                }
            }
        }
    }

    override fun onMyCartResp(it: CartEntity) {
        mCartEntity = it
        try {
            GlobalScope.launch { launch(Dispatchers.IO) { localCart.deleteAll() } }
        } catch (e: Exception) {
        }

        mCartItems.clear()
        mCartItems.addAll(it.data.cart_item_list)

        if (mCartItems.isEmpty()){
            lblSimilarItem.visibility=View.GONE
            clCoupon.visibility=View.GONE
            checkoutLayout.visibility=View.GONE
        }else{
            lblSimilarItem.visibility=View.VISIBLE
            clCoupon.visibility=View.VISIBLE
            checkoutLayout.visibility=View.VISIBLE
        }


        mMyCartAdapter.notifyDataSetChanged()
        tvSubtotal.text = it.data.order_summary.subtotal
        tvShipping.text = it.data.order_summary.shipping
        tvEstimatedTax.text = it.data.order_summary.estimated_tax
        tvPriceTagDiscount.text = it.data.order_summary.gbrx_discount_coupon_amount
        tvDiscCoupon.text = it.data.order_summary.gbrx_discount_coupon_name
        tvTotal.text = it.data.order_summary.total
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token) { }
        } catch (e: Exception) {
        }

        if (it.data.order_summary.gbrx_discount_coupon_description.isNotEmpty()){
            mIvInfo.visibility=View.VISIBLE
        }else{
            mIvInfo.visibility=View.GONE
        }

        couponDescription=it.data.order_summary.gbrx_discount_coupon_description


    }

    override fun onGeneratedToken(lastAction: String) {

    }

    override fun enableButton() {

    }

    override fun disableButton() {

    }

    fun updateToCart(product_id: String, qty: String, size: String, action: String, pos: Int) {
        if (isInternetConnected())
            JsonObject().apply {
                addProperty("product_id", product_id)
                addProperty("quantity", qty)
                addProperty("size", size)
                presenter.updateItemCount(this, action, pos)
            }
    }

    override fun onUpdateToCartResp(
        it: MyCartEntity,
        action: String,
        pos: Int
    ) {
        if (action.equals("remove").not()) {
            toast(
                "Item quantity ${if (action.equals("plus")) "increased" else "decreased"} successfully!!",
                true
            )
        } else {
            mCartItems.removeAt(pos)

            if (mCartItems.isEmpty()){
                lblSimilarItem.visibility=View.GONE
                clCoupon.visibility=View.GONE
                checkoutLayout.visibility=View.GONE
            }else{
                lblSimilarItem.visibility=View.VISIBLE
                clCoupon.visibility=View.VISIBLE
                checkoutLayout.visibility=View.VISIBLE
            }


            mMyCartAdapter.notifyDataSetChanged()
            Intent("UPDATE_CART").apply {
                putExtra("cart_updated", true)
                LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(this)
            }

            toast("Item removed successfully!!", true)

            if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
                if (isInternetConnected()) checkToUpdateServer()
            }
        }

        if (!it.order_summary!!.final_amount.isEmpty()){
            mCartEntity!!.data.order_summary = it.order_summary!!
        }
        tvSubtotal.text = it.order_summary.subtotal
        tvShipping.text = it.order_summary.shipping
        tvEstimatedTax.text = it.order_summary.estimated_tax
        tvPriceTagDiscount.text = it.order_summary.discount
        tvTotal.text = it.order_summary.final_amount
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(it.auth.new_jwt_token) { }
        } catch (e: Exception) {
        }

    }

    override fun invoke(p1: String, p2: Int) {
        when (p1) {
            "plus" -> {
                if (mCartItems[p2].size.isEmpty()) {
                    if (mCartItems[p2].quantity.toInt() ==100)
                        mCartItems[p2].quantity =
                            mCartItems[p2].quantity.toInt().plus(150).toString()
                    else if (mCartItems[p2].quantity.toInt() ==250)
                        mCartItems[p2].quantity =
                            mCartItems[p2].quantity.toInt().plus(250).toString()
                    else if (mCartItems[p2].quantity.toInt() ==500)
                        mCartItems[p2].quantity =
                            mCartItems[p2].quantity.toInt().plus(500).toString()
                    else if (mCartItems[p2].quantity.toInt() ==1000)
                        mCartItems[p2].quantity =
                            mCartItems[p2].quantity.toInt().plus(0).toString()
                    else return
                } else mCartItems[p2].quantity = mCartItems[p2].quantity.toInt().plus(1).toString()
                if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
                    updateToCart(
                        mCartItems[p2].product_id,
                        mCartItems[p2].quantity,
                        mCartItems[p2].size,
                        p1, p2
                    )
                } else {
                    updateCartItem(p2, mCartItems[p2].quantity, p1)
                }

                if (!mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
                    var total = 0.00
                    mCartItems.forEach {
                        if (it.discounted_price.isNotEmpty()){
                            var quantity=it.quantity
                            var totalAmount=it.discounted_price.replace("$", "").toDouble() * quantity.toInt()
                            total = total.plus(totalAmount)
                        }

                    }

                    if (!mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
                        var total_new=roundOffDecimal(total)
                        tvSubtotal.text = "$$total_new"
                        tvTotal.text = "$$total_new"
                    }else{
                        tvSubtotal.text = "$$total"
                        tvTotal.text = "$$total"
                    }

                }


                mMyCartAdapter.notifyItemChanged(p2)
            }
            "minus" -> {
                if (mCartItems[p2].size.isEmpty()) {
                    if (mCartItems[p2].quantity.toInt() ==100)
                        mCartItems[p2].quantity =
                            mCartItems[p2].quantity.toInt().minus(0).toString()
                    else if (mCartItems[p2].quantity.toInt() ==250)
                        mCartItems[p2].quantity =
                            mCartItems[p2].quantity.toInt().minus(150).toString()
                    else if (mCartItems[p2].quantity.toInt() ==500)
                        mCartItems[p2].quantity =
                            mCartItems[p2].quantity.toInt().minus(250).toString()
                    else if (mCartItems[p2].quantity.toInt() ==1000)
                        mCartItems[p2].quantity =
                            mCartItems[p2].quantity.toInt().minus(500).toString()
                    else return
                } else {
                    if (mCartItems[p2].quantity.toInt() > 1) mCartItems[p2].quantity =
                        mCartItems[p2].quantity.toInt().minus(1).toString()
                    else return
                }
                if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
                    updateToCart(
                        mCartItems[p2].product_id,
                        mCartItems[p2].quantity,
                        mCartItems[p2].size,
                        p1, p2
                    )
                } else
                    updateCartItem(p2, mCartItems[p2].quantity, p1)
                mMyCartAdapter.notifyItemChanged(p2)

                if (!mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
                    var total = 0.00
                    mCartItems.forEach {
                        if (it.discounted_price.isNotEmpty()){
                            var quantity=it.quantity
                            var totalAmount=it.discounted_price.replace("$", "").toDouble() * quantity.toInt()
                            total = total.plus(totalAmount)
                        }

                    }
                    if (!mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
                        var total_new=roundOffDecimal(total)
                        tvSubtotal.text = "$$total_new"
                        tvTotal.text = "$$total_new"
                    }else{
                        tvSubtotal.text = "$$total"
                        tvTotal.text = "$$total"
                    }
                }


                if (mCartItems.isEmpty()){
                    clCoupon.visibility=View.GONE
                    checkoutLayout.visibility=View.GONE
                }else{
                    clCoupon.visibility=View.VISIBLE
                    checkoutLayout.visibility=View.VISIBLE
                }
            }
            "remove" -> {
                if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN).not()) {
                    deleteItem(p2) {
                        requireActivity().runOnUiThread {
                            mCartItems.removeAt(p2)

                            if (mCartItems.isEmpty()){
                                lblSimilarItem.visibility=View.GONE
                                clCoupon.visibility=View.GONE
                                checkoutLayout.visibility=View.GONE
                            }else{
                                lblSimilarItem.visibility=View.VISIBLE
                                clCoupon.visibility=View.VISIBLE
                                checkoutLayout.visibility=View.VISIBLE
                            }


                            mMyCartAdapter.notifyDataSetChanged()
                        }
                    }
                } else{
                    updateToCart(mCartItems[p2].product_id, "0", mCartItems[p2].size, p1, p2)
                }


                if (!mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
                    var total = 0.00
                    mCartItems.forEach {
                        if (it.discounted_price.isNotEmpty()){
                            var quantity=it.quantity
                            var totalAmount=it.discounted_price.replace("$", "").toDouble() * quantity.toInt()
                            total = total.plus(totalAmount)
                        }

                    }
                    if (!mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
                        var total_new=roundOffDecimal(total)
                        tvSubtotal.text = "$$total_new"
                        tvTotal.text = "$$total_new"
                    }else{
                        tvSubtotal.text = "$$total"
                        tvTotal.text = "$$total"
                    }
                }
            }




        }
    }

    override fun onClick(p0: View) {
        when (p0) {
            btnCheckout -> {
                if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN).not()) {
                    AllinOneDialog("Sign In to Check out",
                        "Please sign in first to check out.",
                        "Cancel",
                        "Login",
                        {},
                        {
                            requireActivity().startActivity(
                                Intent(requireActivity(), LoginEmailPasswordActivity::class.java).putExtra("page","cart")
                            )
                        })
                } else {
                    StoreCheckoutFragment.storeData = mCartEntity

//                    mainActivity.addMainFragment(R.id.homeContainer, StoreCheckoutFragment())
                    mainActivity.addMainFragment(R.id.homeContainer, ShippingFragment())
                }
            }
        }
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


    override fun onStart() {
        super.onStart()

        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(broardReceiverTwo, IntentFilter("GO_BACK")
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(broardReceiverTwo)
    }


    val broardReceiverTwo = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(Intent("GO_BACK"))
            requireActivity().onBackPressed()
        }
    }

    fun roundOffDecimal(number: Double): Double? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }
}