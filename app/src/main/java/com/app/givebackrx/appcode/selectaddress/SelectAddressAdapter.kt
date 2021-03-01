package com.app.givebackrx.appcode.selectaddress

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.DataList
import com.app.givebackrx.utils.extension.onOffVisibility

class SelectAddressAdapter(
    val mAddressList: MutableList<DataList>, val clickItem: (String, Int) -> Unit,val type:String
) : RecyclerView.Adapter<SelectAddressAdapter.VHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_add_address, parent, false)
        )
    }

    override fun getItemCount(): Int = mAddressList.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            tvName.text = mAddressList[position].first_name+" "+mAddressList[position].last_name
            tvAddress.text =/*"${mAddressList[position].apt_suit_number} ${mAddressList[position].address}"*/  if (mAddressList[position].apt_suit_number.isNotEmpty()){mAddressList[position].apt_suit_number +", "+ mAddressList[position].address+",\n"+ mAddressList[position].city+", "+ mAddressList[position].state+", "+ mAddressList[position].country+",\n"+ mAddressList[position].phone}else{mAddressList[position].address+",\n"+ mAddressList[position].city+", "+ mAddressList[position].state+", "+ mAddressList[position].country+",\n"+ mAddressList[position].phone}
            tvPhone.visibility = View.GONE
            tvPhone.text = mAddressList[position].phone
            btnAddAddress.onOffVisibility(position==mAddressList.size-1)
            if (type.equals("Shipping")){
                btnDefault.text=if (mAddressList[position].shipping_address) btnDefault.context.getString(R.string.selected_address) else btnDefault.context.getString(R.string.select_address)
            }else{
                btnDefault.text=if (mAddressList[position].billing_address) btnDefault.context.getString(R.string.selected_address) else btnDefault.context.getString(R.string.select_address)
            }

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