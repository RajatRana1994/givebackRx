package com.app.givebackrx.appcode.mycart

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.CartItem
import com.app.givebackrx.utils.extension.loadOrigImage
import com.app.givebackrx.utils.extension.onOffVisibility

class MyCartAdapter(
    val mCategoryList: MutableList<CartItem>, val clickItem: (String, Int) -> Unit
) : RecyclerView.Adapter<MyCartAdapter.VHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_cart_item, parent, false)
        )
    }

    override fun getItemCount(): Int = mCategoryList.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            tvName.text = Html.fromHtml(mCategoryList[position].display_product_name)
            tvPrice.text = Html.fromHtml(mCategoryList[position].discounted_price)
            tvSize.text = Html.fromHtml(if(mCategoryList[position].size.isNotEmpty()) "Size: ${mCategoryList[position].size}" else "")
            tvQtyCount.text = Html.fromHtml(mCategoryList[position].quantity)
            mIvImage.loadOrigImage(mCategoryList[position].image_url)
            ivAdd.setOnClickListener {
                clickItem.invoke("plus", position)
            }
            ivMinus.setOnClickListener {
                clickItem.invoke("minus", position)
            }
            tvRemove.setOnClickListener {
                clickItem.invoke("remove", position)
            }
            tvCharityCardTag.onOffVisibility(mCategoryList[position].size.isEmpty())
        }
    }

    class VHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val mIvImage = view.findViewById<ImageView>(R.id.mIvImage)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)
        val tvSize = view.findViewById<TextView>(R.id.tvSize)
        val ivAdd = view.findViewById<ImageView>(R.id.ivAdd)
        val tvQtyCount = view.findViewById<TextView>(R.id.tvQtyCount)
        val ivMinus = view.findViewById<ImageView>(R.id.ivMinus)
        val tvRemove = view.findViewById<TextView>(R.id.tvRemove)
        val tvCharityCardTag = view.findViewById<TextView>(R.id.tvCharityCardTag)
    }

}