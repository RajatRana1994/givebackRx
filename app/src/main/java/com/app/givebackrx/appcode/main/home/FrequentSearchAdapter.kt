package com.app.givebackrx.appcode.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.listeners.SingleListCLickListener
import com.app.givebackrx.localdb.FreqSearchEntity

class FrequentSearchAdapter(val mFreqSearchList: MutableList<FreqSearchEntity>, val listener: SingleListCLickListener) : RecyclerView.Adapter<FrequentSearchAdapter.VHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_freq_seraches,parent,false))
    }

    override fun getItemCount(): Int = mFreqSearchList.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            tvName.text=mFreqSearchList[position].display_drug_name
            view.setOnClickListener { listener.onSingleListClick(mFreqSearchList.elementAt(position),position) }
        }
    }

    class VHolder(val view: View):RecyclerView.ViewHolder(view){
        val tvName=view.findViewById<TextView>(R.id.tvName)

    }
}