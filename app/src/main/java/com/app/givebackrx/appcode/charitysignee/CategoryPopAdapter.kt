package com.app.givebackrx.appcode.charitysignee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.PharmacyFilterEntity
import com.app.givebackrx.listeners.SingleListCLickListener

class CategoryPopAdapter(
    val mCategoryList: MutableList<PharmacyFilterEntity>,
    val listener: SingleListCLickListener
) : RecyclerView.Adapter<CategoryPopAdapter.VHolder>() {
    var mPharmacyData= mutableListOf<PharmacyFilterEntity>()

    init {
        mPharmacyData=mCategoryList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder = VHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_filter_single, parent, false)
    )

    fun replaceList(mPharmacies: MutableList<PharmacyFilterEntity>){
        mPharmacyData=(mPharmacies)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mPharmacyData.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            tvFilterItem.text = mPharmacyData[position].label
            tvFilterItem.setPadding(4,30,4,30)
            tvFilterItem.isChecked = mPharmacyData[position].selected

            tvFilterItem.setOnClickListener {
                mPharmacyData[position].selected = mPharmacyData[position].selected.not()
                notifyItemChanged(position)
                listener.onSingleListClick(position,position)
            }
        }
    }

    class VHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvFilterItem = view.findViewById<CheckedTextView>(R.id.tvFilterItem)
    }
}