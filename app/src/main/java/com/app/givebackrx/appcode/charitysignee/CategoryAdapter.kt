package com.app.givebackrx.appcode.charitysignee

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.PharmacyFilterEntity

class CategoryAdapter(val mCategoryList: MutableList<PharmacyFilterEntity>, val removedOther: () -> Unit
) : RecyclerView.Adapter<CategoryAdapter.VHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_category_charity_signee, parent, false)
        )
    }

    override fun getItemCount(): Int = mCategoryList.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            lblCategory.text = Html.fromHtml(mCategoryList[position].value)

            btnClear.setOnClickListener {
                if (mCategoryList[position].equals("Other"))
                    removedOther.invoke()
                mCategoryList.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    class VHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val lblCategory = view.findViewById<TextView>(R.id.lblCategory)
        val btnClear = view.findViewById<ImageView>(R.id.btnClear)
    }

}