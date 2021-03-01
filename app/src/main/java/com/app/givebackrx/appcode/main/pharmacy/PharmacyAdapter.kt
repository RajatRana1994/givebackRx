package com.app.givebackrx.appcode.main.pharmacy

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.appcode.preSignUpModule.PreSignUpCheckActivity
import com.app.givebackrx.data.entity.PharmacyData
import com.app.givebackrx.data.preferences.PreferenceHelper
import com.app.givebackrx.listeners.SingleListCLickListener
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.onOffVisibility
import com.app.givebackrx.utils.toCamelCase
import java.util.*

class PharmacyAdapter(
    val mPharmacyData: MutableList<PharmacyData>,
    val listener: SingleListCLickListener,
    val mPrefs: PreferenceHelper
) : RecyclerView.Adapter<PharmacyAdapter.VHolder>() {

    var visiblePos = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_pharmacy_item, parent, false)
        )
    }

    override fun getItemCount(): Int = mPharmacyData.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
                if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH)
                        .equals("silver")
                ) {
                    tvShopDiscount.text = "$${mPharmacyData[position].silver_price}"
                    gbRxPrice.text = "$${mPharmacyData[position].silver_price}"
                    btnGBRxSilver.onOffVisibility(false)

                    if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).contains("charity").not()){
                        btnGBRxGold.onOffVisibility(true)
                        textView22.onOffVisibility(true)
                        view1.onOffVisibility(true)
                    }

                    btnGBRxGold.text = "${btnGBRxGold.context.getString(R.string.goldstring)} | $${mPharmacyData[position].gold_price}"
                } else {
                    tvShopDiscount.text = "$${mPharmacyData[position].gold_price}"
                    gbRxPrice.text = "$${mPharmacyData[position].gold_price}"
                    btnGBRxSilver.onOffVisibility(false)
                    btnGBRxGold.onOffVisibility(true)
                    textView22.onOffVisibility(true)
                    view1.onOffVisibility(true)
                    //save more hide hr
                }
            } else {
                tvShopDiscount.text = "$${mPharmacyData[position].marketplace_price}"
                gbRxPrice.text = "$${mPharmacyData[position].marketplace_price}"
                btnGBRxSilver.text = "${btnGBRxGold.context.getString(R.string.silverstring)} | $${mPharmacyData[position].silver_price}"
                btnGBRxGold.text = "${btnGBRxGold.context.getString(R.string.goldstring)} | $${mPharmacyData[position].gold_price}"
                btnGBRxSilver.onOffVisibility(true)
                btnGBRxGold.onOffVisibility(false)
                textView22.onOffVisibility(true)
                view1.onOffVisibility(true)
            }

            if (mPrefs.getKeyValue(PreferenceConstants.LAT).isNotEmpty()) {
                mTvDistance.onOffVisibility(true)
                mTvDistance.text = mPharmacyData[position].distance + " miles"
            }else{
                mTvDistance.onOffVisibility(false)
            }

            tvShopDiscount.setTextColor(
                ContextCompat.getColor(
                    tvShopDiscount.context,
                    if (mPharmacyData[position].is_lowest_three.equals("true")) R.color.green_color else R.color.dark_gray_color
                )
            )
            tvShopDiscount.setCompoundDrawablesWithIntrinsicBounds(
                if (mPharmacyData[position].is_lowest.equals(
                        "true"
                    )
                ) R.drawable.ic_feedback else 0, 0, 0, 0
            )

            estimateRetail.text = "$${mPharmacyData[position].retail_price}"
            yourSaving.text = "$${mPharmacyData[position].saving_amount}"
            tvShopName.text = mPharmacyData[position].pharmacy_name.toCamelCase()


            if (mPharmacyData[position].visible) {
                ViewAnimationUtils.expand(groupExpanded)
                gbRxPrice.onOffVisibility(true)
                yourSaving.onOffVisibility(true)
                tvSavingBreakdown.onOffVisibility(true)
                tvEstimateRetail.onOffVisibility(true)
                pharmacyAdress.onOffVisibility(true)
                tvYourSaving.onOffVisibility(true)
                tvGetCoupon1.onOffVisibility(false)
                tvPharmacyAdress.onOffVisibility(true)
                tvGBRxPrice.onOffVisibility(true)
                estimateRetail.onOffVisibility(true)
                expandAction.rotation = 90f
                if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
                    if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
                            .toLowerCase(Locale.ENGLISH).equals("silver")
                    ) {
                        btnGBRxSilver.onOffVisibility(false)

                        if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).contains("charity").not()){
                            btnGBRxGold.onOffVisibility(true)
                            textView22.onOffVisibility(true)
                            view1.onOffVisibility(true)
                        }

                    } else {
                        btnGBRxSilver.onOffVisibility(false)
                        btnGBRxGold.onOffVisibility(false)
                        textView22.onOffVisibility(false)
                        view1.onOffVisibility(false)
                    }
                } else {
                    btnGBRxSilver.onOffVisibility(true)
                    btnGBRxGold.onOffVisibility(false)
                    textView22.onOffVisibility(true)
                    view1.onOffVisibility(true)
                }

                if (mPharmacyData[position].saving_amount.contains("-") || mPharmacyData[position].saving_percent.contains(
                        "-"
                    )
                ) {
                    tvSavingBreakdown.onOffVisibility(false)
                    tvEstimateRetail.onOffVisibility(false)
                    estimateRetail.onOffVisibility(false)
                    tvGBRxPrice.onOffVisibility(false)
                    gbRxPrice.onOffVisibility(false)
                    tvYourSaving.onOffVisibility(false)
                    yourSaving.onOffVisibility(false)
                    tvSavePer.onOffVisibility(false)
                    tvMsg.onOffVisibility(true)
                    tvMsg.text = tvMsg.context.getString(R.string.pharmacy_list_msg)
                } else {
                    tvSavingBreakdown.onOffVisibility(true)
                    tvEstimateRetail.onOffVisibility(true)
                    estimateRetail.onOffVisibility(true)
                    tvGBRxPrice.onOffVisibility(true)
                    gbRxPrice.onOffVisibility(true)
                    tvYourSaving.onOffVisibility(true)
                    yourSaving.onOffVisibility(true)
                    tvSavePer.onOffVisibility(true)
                    tvMsg.onOffVisibility(false)
                }

            } else {
                expandAction.rotation = 270f
                groupExpanded.onOffVisibility(false)
                gbRxPrice.onOffVisibility(false)
                yourSaving.onOffVisibility(false)
                tvSavingBreakdown.onOffVisibility(false)
                tvEstimateRetail.onOffVisibility(false)
                pharmacyAdress.onOffVisibility(false)
                tvYourSaving.onOffVisibility(false)
                tvGetCoupon1.onOffVisibility(false)
                tvPharmacyAdress.onOffVisibility(false)
                tvGBRxPrice.onOffVisibility(false)
                estimateRetail.onOffVisibility(false)

                if (mPharmacyData[position].saving_amount.contains("-") || mPharmacyData[position].saving_percent.contains(
                        "-"
                    )
                )
                    tvSavePer.onOffVisibility(false)
                else
                    tvSavePer.onOffVisibility(true)
                btnGBRxGold.onOffVisibility(false)
                textView22.onOffVisibility(false)
                view1.onOffVisibility(false)
            }


            pharmacyAdress.text = getAddress(mPharmacyData[position])
            tvSavePer.text = tvSavePer.context.getString(R.string.save_per, "${mPharmacyData[position].saving_percent}%")
            expandAction.setOnClickListener {
                if (visiblePos != -1 && visiblePos != position) {
                    mPharmacyData[visiblePos].visible = false
                    notifyItemChanged(visiblePos)
                }
                visiblePos = position
                mPharmacyData[position].visible = !mPharmacyData[position].visible
                notifyItemChanged(position)
            }
            tvGetCoupon.setOnClickListener {
                listener.onSingleListClick(
                    "detail",
                    position
                )
            }
            tvGetCoupon1.setOnClickListener { tvGetCoupon.performClick() }
            btnGBRxSilver.setOnClickListener { btnGBRxSilver.context.startActivity(Intent(btnGBRxSilver.context,PreSignUpCheckActivity::class.java)) }
            btnGBRxGold.setOnClickListener {  listener.onSingleListClick(
                "gold_update",
                position
            )
            }
            tvShopName.setOnClickListener {  listener.onSingleListClick(
                "zoom_map",
                position
            )
            }
        }
//        holder.apply {
//            if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
//                if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH)
//                        .equals("silver")
//                ) {
//                    tvShopDiscount.text = "$${mPharmacyData[position].silver_price}"
//                    gbRxPrice.text = "$${mPharmacyData[position].silver_price}"
//                    btnGBRxSilver.onOffVisibility(false)
//
//                    if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).contains("charity").not()){
//                        btnGBRxGold.onOffVisibility(true)
//                        textView22.onOffVisibility(true)
//                        view1.onOffVisibility(true)
//                    }
//
//                    btnGBRxGold.text = "${btnGBRxGold.context.getString(R.string.goldstring)} | $${mPharmacyData[position].gold_price}"
//                } else {
//                    tvShopDiscount.text = "$${mPharmacyData[position].gold_price}"
//                    gbRxPrice.text = "$${mPharmacyData[position].gold_price}"
//                    btnGBRxSilver.onOffVisibility(false)
//                    btnGBRxGold.onOffVisibility(true)
//                    textView22.onOffVisibility(true)
//                    view1.onOffVisibility(true)
//                    //save more hide hr
//                }
//            } else {
//                tvShopDiscount.text = "$${mPharmacyData[position].marketplace_price}"
//                gbRxPrice.text = "$${mPharmacyData[position].marketplace_price}"
//                btnGBRxSilver.text = "${btnGBRxGold.context.getString(R.string.silverstring)} | $${mPharmacyData[position].silver_price}"
//                btnGBRxGold.text = "${btnGBRxGold.context.getString(R.string.goldstring)} | $${mPharmacyData[position].gold_price}"
//                btnGBRxSilver.onOffVisibility(true)
//                btnGBRxGold.onOffVisibility(false)
//                textView22.onOffVisibility(true)
//                view1.onOffVisibility(true)
//            }
//
//            if (mPrefs.getKeyValue(PreferenceConstants.LAT).isNotEmpty()) {
//                mTvDistance.onOffVisibility(true)
//                mTvDistance.text = mPharmacyData[position].distance + " miles"
//            }else{
//                mTvDistance.onOffVisibility(false)
//            }
//
//            tvShopDiscount.setTextColor(
//                ContextCompat.getColor(
//                    tvShopDiscount.context,
//                    if (mPharmacyData[position].is_lowest_three.equals("true")) R.color.green_color else R.color.dark_gray_color
//                )
//            )
//            tvShopDiscount.setCompoundDrawablesWithIntrinsicBounds(
//                if (mPharmacyData[position].is_lowest.equals(
//                        "true"
//                    )
//                ) R.drawable.ic_feedback else 0, 0, 0, 0
//            )
//
//            estimateRetail.text = "$${mPharmacyData[position].retail_price}"
//            yourSaving.text = "$${mPharmacyData[position].saving_amount}"
//            tvShopName.text = mPharmacyData[position].pharmacy_name.toCamelCase()
//
//
//            if (mPharmacyData[position].visible) {
//                ViewAnimationUtils.expand(groupExpanded)
//                gbRxPrice.onOffVisibility(true)
//                yourSaving.onOffVisibility(true)
//                tvSavingBreakdown.onOffVisibility(true)
//                tvEstimateRetail.onOffVisibility(true)
//                pharmacyAdress.onOffVisibility(true)
//                tvYourSaving.onOffVisibility(true)
//                tvGetCoupon1.onOffVisibility(false)
//                tvPharmacyAdress.onOffVisibility(true)
//                tvGBRxPrice.onOffVisibility(true)
//                estimateRetail.onOffVisibility(true)
//                expandAction.rotation = 90f
//                if (mPrefs.isUserLoggedIn(PreferenceConstants.USER_LOGGED_IN)) {
//                    if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE)
//                            .toLowerCase(Locale.ENGLISH).equals("silver")
//                    ) {
//                        btnGBRxSilver.onOffVisibility(false)
//
//                        if (mPrefs.getKeyValue(PreferenceConstants.USER_TYPE).contains("charity").not()){
//                            btnGBRxGold.onOffVisibility(true)
//                            textView22.onOffVisibility(true)
//                            view1.onOffVisibility(true)
//                        }
//
//                    } else {
//                        btnGBRxSilver.onOffVisibility(false)
//                        btnGBRxGold.onOffVisibility(false)
//                        textView22.onOffVisibility(false)
//                        view1.onOffVisibility(false)
//                    }
//                } else {
//                    btnGBRxSilver.onOffVisibility(true)
//                    btnGBRxGold.onOffVisibility(false)
//                    textView22.onOffVisibility(true)
//                    view1.onOffVisibility(true)
//                }
//
//                if (mPharmacyData[position].saving_amount.contains("-") || mPharmacyData[position].saving_percent.contains(
//                        "-"
//                    )
//                ) {
//                    tvSavingBreakdown.onOffVisibility(false)
//                    tvEstimateRetail.onOffVisibility(false)
//                    estimateRetail.onOffVisibility(false)
//                    tvGBRxPrice.onOffVisibility(false)
//                    gbRxPrice.onOffVisibility(false)
//                    tvYourSaving.onOffVisibility(false)
//                    yourSaving.onOffVisibility(false)
//                    tvSavePer.onOffVisibility(false)
//                    tvMsg.onOffVisibility(true)
//                    tvMsg.text = tvMsg.context.getString(R.string.pharmacy_list_msg)
//                } else {
//                    tvSavingBreakdown.onOffVisibility(true)
//                    tvEstimateRetail.onOffVisibility(true)
//                    estimateRetail.onOffVisibility(true)
//                    tvGBRxPrice.onOffVisibility(true)
//                    gbRxPrice.onOffVisibility(true)
//                    tvYourSaving.onOffVisibility(true)
//                    yourSaving.onOffVisibility(true)
//                    tvSavePer.onOffVisibility(true)
//                    tvMsg.onOffVisibility(false)
//                }
//
//            } else {
//                expandAction.rotation = 270f
//                groupExpanded.onOffVisibility(false)
//                gbRxPrice.onOffVisibility(false)
//                yourSaving.onOffVisibility(false)
//                tvSavingBreakdown.onOffVisibility(false)
//                tvEstimateRetail.onOffVisibility(false)
//                pharmacyAdress.onOffVisibility(false)
//                tvYourSaving.onOffVisibility(false)
//                tvGetCoupon1.onOffVisibility(false)
//                tvPharmacyAdress.onOffVisibility(false)
//                tvGBRxPrice.onOffVisibility(false)
//                estimateRetail.onOffVisibility(false)
//
//                if (mPharmacyData[position].saving_amount.contains("-") || mPharmacyData[position].saving_percent.contains(
//                        "-"
//                    )
//                )
//                    tvSavePer.onOffVisibility(false)
//                else
//                    tvSavePer.onOffVisibility(true)
//                btnGBRxGold.onOffVisibility(false)
//                textView22.onOffVisibility(false)
//                view1.onOffVisibility(false)
//            }
//
//
//            pharmacyAdress.text = getAddress(mPharmacyData[position])
//            tvSavePer.text = tvSavePer.context.getString(R.string.save_per, "${mPharmacyData[position].saving_percent}%")
//            expandAction.setOnClickListener {
//                if (visiblePos != -1 && visiblePos != position) {
//                    mPharmacyData[visiblePos].visible = false
//                    notifyItemChanged(visiblePos)
//                }
//                visiblePos = position
//                mPharmacyData[position].visible = !mPharmacyData[position].visible
//                notifyItemChanged(position)
//            }
//            tvGetCoupon.setOnClickListener {
//                listener.onSingleListClick(
//                    "detail",
//                    position
//                )
//            }
//            tvGetCoupon1.setOnClickListener { tvGetCoupon.performClick() }
//            btnGBRxSilver.setOnClickListener { btnGBRxSilver.context.startActivity(Intent(btnGBRxSilver.context,PreSignUpCheckActivity::class.java)) }
//            btnGBRxGold.setOnClickListener {  listener.onSingleListClick(
//                "gold_update",
//                position
//            )
//            }
//            tvShopName.setOnClickListener {  listener.onSingleListClick(
//                "zoom_map",
//                position
//            )
//            }
//        }

    }

    class VHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvShopName = view.findViewById<TextView>(R.id.tvShopName)
        val tvShopDiscount = view.findViewById<TextView>(R.id.tvShopDiscount)
        val mTvDistance = view.findViewById<TextView>(R.id.mTvDistance)
        val tvSavePer = view.findViewById<TextView>(R.id.tvSavePer)
        val tvGetCoupon = view.findViewById<TextView>(R.id.tvGetCoupon)
        val tvGetCoupon1 = view.findViewById<TextView>(R.id.tvGetCoupon1)
        val pharmacyAdress = view.findViewById<TextView>(R.id.pharmacyAdress)
        val estimateRetail = view.findViewById<TextView>(R.id.estimateRetail)
        val gbRxPrice = view.findViewById<TextView>(R.id.gbRxPrice)
        val yourSaving = view.findViewById<TextView>(R.id.yourSaving)
        val btnGBRxSilver = view.findViewById<TextView>(R.id.btnGBRxSilver)
        val btnGBRxGold = view.findViewById<TextView>(R.id.btnGBRxGold)

        val textView22 = view.findViewById<TextView>(R.id.textView22)
        val tvSavingBreakdown = view.findViewById<TextView>(R.id.tvSavingBreakdown)
        val tvEstimateRetail = view.findViewById<TextView>(R.id.tvEstimateRetail)
        val tvYourSaving = view.findViewById<TextView>(R.id.tvYourSaving)
        val tvPharmacyAdress = view.findViewById<TextView>(R.id.tvPharmacyAdress)
        val tvGBRxPrice = view.findViewById<TextView>(R.id.tvGBRxPrice)
        val groupExpanded = view.findViewById<ConstraintLayout>(R.id.groupExpanded)
        val tvMsg = view.findViewById<TextView>(R.id.tvMsg)

        val expandAction = view.findViewById<ImageView>(R.id.expandAction)
        val view1 = view.findViewById<View>(R.id.view1)
    }

    fun getAddress(pharmacyData: PharmacyData): String {
        var address = pharmacyData.street_address
        address += if (address.isEmpty()) pharmacyData.city else ", ${pharmacyData.city}"
        address += if (address.isEmpty()) pharmacyData.state else ", ${pharmacyData.state}"
        address += if (address.isEmpty()) pharmacyData.zip_code else ", ${pharmacyData.zip_code}"
        return address
    }
}