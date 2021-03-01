package com.app.givebackrx.appcode.storedetail

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.StoreProduct
import com.app.givebackrx.utils.extension.loadOrigImage

class StoreItemSizeAdapter(
    val mSizeList: MutableList<StoreDetailFragment.ItemSizeModel>, val clickItem: (Int) -> Unit
) : RecyclerView.Adapter<StoreItemSizeAdapter.VHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item_size, parent, false)
        )
    }

    override fun getItemCount(): Int = mSizeList.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            tvSize.isChecked=mSizeList[position].choosed
            tvSize.text=mSizeList[position].size
            tvSize.setOnClickListener {
                mSizeList.forEach { !it.choosed }
                notifyDataSetChanged()
                mSizeList[position].choosed=true
                clickItem.invoke(position)
            }
        }
    }

    class VHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvSize = view.findViewById<CheckBox>(R.id.tvSize)
    }

}