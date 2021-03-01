package com.app.givebackrx.appcode.settings.purchase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.PharmacyData
import com.app.givebackrx.data.entity.RecentPurchase
import com.app.givebackrx.listeners.SingleListCLickListener
import com.app.givebackrx.utils.extension.loadOrigImage

class RecentPurchaseAdapter(
    var mRecPurchaseFiltered: MutableList<RecentPurchase>,
    val listener: SingleListCLickListener
) : RecyclerView.Adapter<RecentPurchaseAdapter.VHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_items_recentpurchases, parent, false)
        )
    }

    override fun getItemCount(): Int = mRecPurchaseFiltered.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            mIvImage.loadOrigImage(mRecPurchaseFiltered[position].image_url)
            mTvDrugName.text = mRecPurchaseFiltered[position].drug_name__c
            mTvDosaageQuant.text =
                "${mRecPurchaseFiltered[position].dosage} | ${mRecPurchaseFiltered[position].quantity}"
            mTvDate.text = ""
            tvPharmacy.text = mRecPurchaseFiltered[position].pharmacy
            tvPurchasedOn.text = mRecPurchaseFiltered[position].purchased_on
            tvPurchasedBy.text = mRecPurchaseFiltered[position].purchased_by

            btnDownload.setOnClickListener {
                listener.onSingleListClick(mRecPurchaseFiltered[position], position)
            }
        }
    }

    class VHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val mIvImage = view.findViewById<ImageView>(R.id.mIvImage)
        val mTvDrugName = view.findViewById<TextView>(R.id.mTvDrugName)
        val mTvDosaageQuant = view.findViewById<TextView>(R.id.mTvDosaageQuant)
        val mTvDate = view.findViewById<TextView>(R.id.mTvDate)
        val tvPharmacy = view.findViewById<TextView>(R.id.tvPharmacy)
        val tvPurchasedOn = view.findViewById<TextView>(R.id.tvPurchasedOn)
        val tvPurchasedBy = view.findViewById<TextView>(R.id.tvPurchasedBy)
        val btnDownload = view.findViewById<TextView>(R.id.btnDownload)
    }

}