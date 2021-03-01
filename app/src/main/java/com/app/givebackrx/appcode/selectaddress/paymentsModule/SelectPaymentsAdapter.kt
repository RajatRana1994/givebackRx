package com.app.givebackrx.appcode.selectaddress.paymentsModule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.DataPay
import com.app.givebackrx.utils.extension.onOffVisibility

class SelectPaymentsAdapter(
    val mAddressList: MutableList<DataPay>, val clickItem: (String, Int) -> Unit
) : RecyclerView.Adapter<SelectPaymentsAdapter.VHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item_payments, parent, false)
        )
    }

    override fun getItemCount(): Int = mAddressList.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {

            tvAddress.text =mAddressList[position].first_name +" "+ mAddressList[position].last_name+"\n"+ mAddressList[position].card_number+"\n"+ "Exp: "+mAddressList[position].expiry_month+"/"+ mAddressList[position].expiry_year
            tvPhone.visibility = View.GONE
            tvPhone.text = mAddressList[position].card_number
            btnAddAddress.onOffVisibility(position==mAddressList.size-1)

            btnDefault.text=if (mAddressList[position].selected_card) "Selected Card" else "Select Card"

            btnAddAddress.setOnClickListener {
                clickItem.invoke("add", position)
            }
             btnDefault.setOnClickListener {
                clickItem.invoke("default", position)
            }
            ivEdit.setOnClickListener {
                clickItem.invoke("edit", position)
            }
            ivDelete.setOnClickListener {
                clickItem.invoke("delete", position)
            }
        }
    }

    class VHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val ivEdit = view.findViewById<ImageView>(R.id.ivEdit)
        val ivDelete = view.findViewById<ImageView>(R.id.ivDelete)
        val tvAddress = view.findViewById<TextView>(R.id.tvAddress)
        val tvPhone = view.findViewById<TextView>(R.id.tvPhone)
        val btnDefault = view.findViewById<TextView>(R.id.btnDefault)
        val btnAddAddress = view.findViewById<FrameLayout>(R.id.btnAddAddress)
    }

}