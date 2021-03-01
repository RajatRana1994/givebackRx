package com.app.givebackrx.appcode.store

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.StoreProduct
import com.app.givebackrx.utils.extension.loadOrigImage

class StoreAdapter(
    val mCategoryList: MutableList<StoreProduct>, val clickItem: (Int) -> Unit
) : RecyclerView.Adapter<StoreAdapter.VHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_store_item, parent, false)
        )
    }

    override fun getItemCount(): Int = mCategoryList.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            tvName.text = Html.fromHtml(mCategoryList[position].display_product_name)
            tvPrice.text = Html.fromHtml(mCategoryList[position].discount_price)
            mIvImage.loadOrigImage(mCategoryList[position].display_url)
            view.setOnClickListener {
                clickItem.invoke(position)
            }
        }
    }

    class VHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val mIvImage = view.findViewById<ImageView>(R.id.mIvImage)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)
    }

}