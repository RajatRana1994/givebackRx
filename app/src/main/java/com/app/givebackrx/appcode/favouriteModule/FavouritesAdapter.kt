package com.app.givebackrx.appcode.settings.favouriteModule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.FavoriteMedication
import com.app.givebackrx.data.preferences.PreferenceHelper
import com.app.givebackrx.listeners.PairListCLickListener
import com.app.givebackrx.utils.PreferenceConstants
import com.app.givebackrx.utils.extension.loadOrigImage
import com.app.givebackrx.utils.extension.onOffVisibility
import java.util.*

class FavouritesAdapter(
    val listener: PairListCLickListener,
    val favMedList: MutableList<FavoriteMedication>,
    val mPrefs: PreferenceHelper
) : RecyclerView.Adapter<FavouritesAdapter.VHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_items_favoruites, parent, false)
        )
    }

    override fun getItemCount(): Int = favMedList.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            mTvGivebackPrice.text =
                if (mPrefs.getKeyValue(PreferenceConstants.MEMBER_TYPE).toLowerCase(Locale.ENGLISH) == "silver") {
                    btnGBRxGold.onOffVisibility(false)
                    divider5.onOffVisibility(false)
                    textView63.onOffVisibility(false)
//                    btnGBRxGold.onOffVisibility(true)
                    favMedList[position].pricing.silver_price
                } else {
                    btnGBRxGold.onOffVisibility(false)
                    divider5.onOffVisibility(false)
                    textView63.onOffVisibility(false)
                    favMedList[position].pricing.gold_price
                }

            mIvImage.loadOrigImage(favMedList[position].image_url)
            mTvDrugName.text = favMedList[position].drug_generic_name
            mTvDosaageQuant.text =
                "${favMedList[position].dosage} | ${favMedList[position].quantity}"
            mTvDate.text = ""
            mTvMarketPrice.text = favMedList[position].pricing.marketplace_price

            btnGBRxGold.setOnClickListener { listener.onFirstListClick("gold", position) }
            tvGetCoupon.setOnClickListener { listener.onFirstListClick("get_coupon", position) }
            mIvEdit.setOnClickListener { listener.onSecondListClick("edit", position) }
            mIvDelete.setOnClickListener { listener.onSecondListClick("delete", position) }
        }
    }

    class VHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val mIvImage = view.findViewById<ImageView>(R.id.mIvImage)
        val mTvDrugName = view.findViewById<TextView>(R.id.mTvDrugName)
        val mTvDosaageQuant = view.findViewById<TextView>(R.id.mTvDosaageQuant)
        val mTvDate = view.findViewById<TextView>(R.id.mTvDate)
        val mTvMarketPrice = view.findViewById<TextView>(R.id.mTvMarketPrice)
        val mTvGivebackPrice = view.findViewById<TextView>(R.id.mTvGivebackPrice)
        val btnGBRxGold = view.findViewById<TextView>(R.id.btnGBRxGold)
        val tvGetCoupon = view.findViewById<TextView>(R.id.tvGetCoupon)
        val divider5 = view.findViewById<View>(R.id.divider5)
        val textView63 = view.findViewById<TextView>(R.id.textView63)
        val mIvEdit = view.findViewById<ImageView>(R.id.mIvEdit)
        val mIvDelete = view.findViewById<ImageView>(R.id.mIvDelete)
    }

}