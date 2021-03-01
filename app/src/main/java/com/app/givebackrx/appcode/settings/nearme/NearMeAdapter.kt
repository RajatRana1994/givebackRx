package com.app.givebackrx.appcode.settings.nearme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.NearMeData
import com.app.givebackrx.data.preferences.PreferenceHelper
import com.app.givebackrx.listeners.SingleListCLickListener

class NearMeAdapter(
    val mNearMeData: MutableList<NearMeData>,
    val listener: SingleListCLickListener) : RecyclerView.Adapter<NearMeAdapter.VHolder>() {

    var visiblePos = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_near_me, parent, false)
        )
    }

    override fun getItemCount(): Int = mNearMeData.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            tvName.text=mNearMeData[position].Name
            tvAddress.text=getAddress(mNearMeData[position])
            view.setOnClickListener { listener.onSingleListClick("",position) }
        }
    }

    class VHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvAddress = view.findViewById<TextView>(R.id.tvAddress)
    }

    fun getAddress(data: NearMeData): String {
        var address = data.Address
        address += if (address.isEmpty()) data.City else ", ${data.City}"
        address += if (address.isEmpty()) data.State else ", ${data.State}"
        address += if (address.isEmpty()) data.ZipCode else ", ${data.ZipCode}"
        return address
    }
}