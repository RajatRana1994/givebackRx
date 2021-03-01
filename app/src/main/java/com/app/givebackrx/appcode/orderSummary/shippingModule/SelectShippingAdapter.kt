package com.app.givebackrx.appcode.orderSummary.shippingModule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.DataPay
import com.app.givebackrx.data.entity.ShippingMethodList
import com.app.givebackrx.utils.extension.onOffVisibility

class SelectShippingAdapter(
        val mAddressList: MutableList<ShippingMethodList>, val clickItem: (String, Int) -> Unit
) : RecyclerView.Adapter<SelectShippingAdapter.VHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_cart_shipping_types, parent, false)
        )
    }

    override fun getItemCount(): Int = mAddressList.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            radioButton.text=mAddressList[position].lable  +"     "+mAddressList[position].shipping_rate /*if (mAddressList[position].estimated_delivery_date.isNotEmpty()){" ("+mAddressList[position].estimated_delivery_date+" )     "+mAddressList[position].shipping_rate}else{*/
//            }

            rbBtn.isChecked = mAddressList[position].selected

            rbBtn.setOnClickListener {
                mAddressList[position].selected = mAddressList[position].selected.not()
                notifyItemChanged(position)
                clickItem.invoke("select", position)
            }

        }
    }

    class VHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val radioButton = view.findViewById<TextView>(R.id.radioButton)
        val rbBtn = view.findViewById<CheckedTextView>(R.id.rbBtn)
    }

}