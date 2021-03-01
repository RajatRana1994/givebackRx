package com.app.givebackrx.appcode.main.pharmacy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.PharmacyFilterEntity
import com.app.givebackrx.listeners.PairListCLickListener
import com.app.givebackrx.listeners.SingleListCLickListener

class PharmacyFilterAdapter(
    val mPharmaciesList: MutableList<PharmacyFilterEntity>,
    val listener: PairListCLickListener
) : RecyclerView.Adapter<PharmacyFilterAdapter.VHolder>() {
    var selectedPos = -1
    var mPharmacyData= mutableListOf<PharmacyFilterEntity>()

    init {
        mPharmacyData=mPharmaciesList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder = VHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_filter_single, parent, false)
    )

    fun replaceList(mPharmacies: MutableList<PharmacyFilterEntity>){
        selectedPos=-1
        mPharmacyData=(mPharmacies)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mPharmacyData.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            tvFilterItem.text = mPharmacyData[position].label
            tvFilterItem.isChecked = mPharmacyData[position].selected
            if (selectedPos == -1 && mPharmacyData[position].selected) selectedPos = position

            tvFilterItem.setOnClickListener {
                if (selectedPos != -1) {
                    mPharmacyData[selectedPos].selected = false
                    notifyItemChanged(selectedPos)
                }
                selectedPos=position
                mPharmacyData[position].selected = true
                listener.onFirstListClick(mPharmacyData[position].type,position)
                notifyItemChanged(position)
            }
        }
    }

    class VHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvFilterItem = view.findViewById<CheckedTextView>(R.id.tvFilterItem)
    }
}