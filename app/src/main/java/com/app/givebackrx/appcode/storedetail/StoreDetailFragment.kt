package com.app.givebackrx.appcode.storedetail

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.givebackrx.R
import com.app.givebackrx.base.BaseFragment
import com.app.givebackrx.data.entity.MyCartEntity
import com.app.givebackrx.data.entity.ProductDetail
import com.app.givebackrx.data.entity.SimilarProduct
import com.app.givebackrx.data.entity.StoreDetailEntity
import com.app.givebackrx.localdb.LocalCartDataBs
import com.app.givebackrx.localdb.LocalCartEntity
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.onOffVisibility
import com.app.givebackrx.utils.extension.toast
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_store.*
import kotlinx.android.synthetic.main.fragment_store_detail.*
import kotlinx.android.synthetic.main.fragment_store_detail.cartBedge
import kotlinx.android.synthetic.main.fragment_store_detail.ivBack
import kotlinx.android.synthetic.main.fragment_store_detail.parentLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoreDetailFragment : BaseFragment(), IStoreDetailView {
    data class ItemSizeModel(var size: String, var choosed: Boolean = false)

    val localCart by lazy { LocalCartDataBs.localCartDb(requireActivity()).localCartDao() }

    var mProductDetail: ProductDetail? = null
    val category_extra: String by lazy { requireArguments().getString("category_extra", "") ?: "" }
    val product_group_extra: String by lazy {
        requireArguments().getString(
            "product_group_extra",
            ""
        ) ?: ""
    }
    val color_extra: String by lazy { requireArguments().getString("color_extra", "") ?: "" }
    val size_extra: String by lazy { requireArguments().getString("size_extra", "") ?: "" }

    @Inject
    lateinit var presenter: StoreDetailPresenter<IStoreDetailView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store_detail, container, false)
    }

    override fun setUp(view: View) {
        presenter.onAttach(this)
        storeProductDetail(category_extra, product_group_extra, color_extra, size_extra)
        ivBack.setOnClickListener { requireActivity().onBackPressed() }
        btnAddToCart.setOnClickListener { addToCart(mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) }
//        tvTitle.setOnClickListener { showCart() }
    }

    fun storeProductDetail(category: String, product_group: String, color: String, size: String) {
        if (isInternetConnected())
            hashMapOf<String, String>().apply {
                put("category", category)
                put("product_group", product_group)
                put("color", color)
                put("size", size)
                if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN))
                    put("membership_type", mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE))
                presenter.storeProductDetail(this)
            }

    }

    override fun onStoreDetailResp(itStore: StoreDetailEntity) {
        parentLayout.onOffVisibility(true)
        mProductDetail = itStore.data.product_detail
        tvName.text = Html.fromHtml(itStore.data.product_detail.display_product_name)
        tvPrice.text = Html.fromHtml(itStore.data.product_detail.discount_price)
        tvProductDetail.text = Html.fromHtml(itStore.data.product_detail.description)

        viewPager.apply {
            adapter = ImageVPAdapter(
                requireActivity(),
                itStore.data.product_detail.images as MutableList<String>
            )
            dots.attachViewPager(this)
        }

        if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
            if (itStore.data.total_cart_items.isNullOrEmpty().not()) {
                cartBedge.apply {
                    text = itStore.data.total_cart_items
                    onOffVisibility(itStore.data.total_cart_items.toInt() > 0)
                }
                if (itStore.data.total_cart_items.toInt() > 0)
                    Intent("UPDATE_LIST").apply {
                        putExtra("cart_updated", true)
                        LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(this)
                    }
            }
        } else getCartCount()

        mutableListOf<ItemSizeModel>().apply {
            itStore.data.product_detail.sizes.forEach {
                this.add(ItemSizeModel(it, it == itStore.data.product_detail.size))
            }
            rvSizeList.adapter = StoreItemSizeAdapter(this) {
                storeProductDetail(
                    itStore.data.product_detail.category,
                    itStore.data.product_detail.product_group,
                    itStore.data.product_detail.color,
                    this[it].size
                )
            }
        }

        rvSimilarItemList.adapter =
            StoreHorizAdapter(itStore.data.similar_products as MutableList<SimilarProduct>) {
                parentLayout.onOffVisibility(false)
                storeProductDetail(
                    itStore.data.similar_products[it].category,
                    itStore.data.similar_products[it].product_group,
                    itStore.data.similar_products[it].color,
                    itStore.data.similar_products[it].size
                )
            }
        try {
            if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).equals("portalUser"))
                decodeToken(itStore.auth.new_jwt_token) { }
        } catch (e: Exception) {
        }


    }


    fun addToCart(userLoggedIn: Boolean) {
        if (userLoggedIn) {
            if (isInternetConnected())
                JsonObject().apply {
                    addProperty("product_id", mProductDetail?.product_id)
                    addProperty("quantity", "1")
                    addProperty("size", mProductDetail?.size)
                    addProperty("redirection_page", "storeDetailPage")
                    presenter.addtoCart(this)
                }
        } else {
            addToCartLocally()
        }
    }


    fun getCartCount() {
        /*GlobalScope.launch {
            launch(Dispatchers.IO) {
                val alls=localCart.getAll()
                requireActivity().runOnUiThread {
                    cartBedge.apply {
                        text=alls.size.toString()
                        onOffVisibility(alls.size>0)
                    }
                }
            }
        }*/



        GlobalScope.launch {
            launch(Dispatchers.IO) {
                val alls = localCart.getAll()
                requireActivity().runOnUiThread {
                    cartBedge.apply {
                        text = alls.size.toString()
                        onOffVisibility(alls.size > 0)
                    }
                }
                /*var total = 0

                localCart.getAll().forEach {
                        if (it.type.equals("charity")) {
                            total = total.toInt().plus(1)
                        } else {
                            total = total.toInt().plus(it.quantity.toInt())
                        }
                    }

                cartBedge.apply {
                    total.toString()
                    onOffVisibility(total.toInt() > 0)
                }*/

            }
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
                            name = mProductDetail?.display_product_name!!,
                            price = mProductDetail?.discount_price!!,
                            imageLogo = if (mProductDetail?.images!!.isEmpty()) "" else mProductDetail?.images!![0],
                            product_id = mProductDetail?.product_id!!,
                            quantity = "1",
                            size = mProductDetail?.size!!,
                            type = "store"
                        )
                    )
                } else {
                    var getEntity: LocalCartEntity? = null
                    dbItems.forEachIndexed { index, entity ->
                        if (entity.product_id == mProductDetail?.product_id!! && entity.size == mProductDetail?.size!!) {
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
                                name = mProductDetail?.display_product_name!!,
                                price = mProductDetail?.discount_price!!,
                                imageLogo = if (mProductDetail?.images!!.isEmpty()) "" else mProductDetail?.images!![0],
                                product_id = mProductDetail?.product_id!!,
                                quantity = "1",
                                size = mProductDetail?.size!!,
                                type = "store"
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
                        storeProductDetail(
                            category_extra,
                            product_group_extra,
                            color_extra,
                            size_extra
                        )
                    else {
                        getCartCount()
                    }
                }
            }
        }
    }

}