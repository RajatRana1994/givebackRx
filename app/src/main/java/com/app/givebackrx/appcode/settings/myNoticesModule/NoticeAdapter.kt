package com.app.givebackrx.appcode.settings.myNoticesModule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.MyNoticeItem
import com.app.givebackrx.data.entity.MyTaskItem
import com.app.givebackrx.data.entity.PharmacyData
import com.app.givebackrx.listeners.SingleListCLickListener
import java.util.*

class NoticeAdapter(
    var mNoticeDataFilter: MutableList<MyNoticeItem>,
    val listener: SingleListCLickListener) : RecyclerView.Adapter<NoticeAdapter.VHolder>(),Filterable {

    val mNoticeData=mNoticeDataFilter
    var visiblePos=-1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_items_notices,parent,false))
    }

    override fun getItemCount(): Int = mNoticeDataFilter.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            tvName.text=mNoticeDataFilter[position].notice_name
            tvAssignDate.text=mNoticeDataFilter[position].created_date
            tvTaskDesc.text=mNoticeDataFilter[position].description
            tvMarkComplete.text=mNoticeDataFilter[position].notice_status

            tvMarkComplete.setTextColor(ContextCompat.getColor(btnBg.context,if (mNoticeDataFilter[position].notice_status.equals("Acknowledged")) R.color.white_color else R.color.pink_color))
            btnBg.setBackgroundColor(ContextCompat.getColor(btnBg.context,if (mNoticeDataFilter[position].notice_status.equals("Acknowledged")) R.color.green_color else R.color.lyt_pink_color))

            tvMarkComplete.setOnClickListener {
                if (mNoticeDataFilter[position].notice_status.equals("Acknowledged").not()){
                    listener.onSingleListClick(mNoticeDataFilter[position],position)
                }
            }
        }
    }

    class VHolder(val view: View):RecyclerView.ViewHolder(view){
        val tvName=view.findViewById<TextView>(R.id.tvName)
        val tvAssignDate=view.findViewById<TextView>(R.id.tvAssignDate)
        val tvTaskDesc=view.findViewById<TextView>(R.id.tvTaskDesc)
        val tvMarkComplete=view.findViewById<TextView>(R.id.tvMarkComplete)
        val btnBg=view.findViewById<TextView>(R.id.tv)
    }

    fun getAddress(pharmacyData: PharmacyData):String {
        var address=pharmacyData.street_address
        address +=if (address.isEmpty()) pharmacyData.city else ", ${pharmacyData.city}"
        address +=if (address.isEmpty()) pharmacyData.state else ", ${pharmacyData.state}"
        address +=if (address.isEmpty()) pharmacyData.zip_code else ", ${pharmacyData.zip_code}"
        return address
    }

    override fun getFilter(): Filter {
         return object :Filter(){
            var result=FilterResults()
            override fun performFiltering(p0: CharSequence?): FilterResults {
                if (p0!!.isEmpty()){
//                    mTasksDataFiltered=mTasksData
                    result.count=mNoticeData.size
                    result.values=mNoticeData
                    return result
                }else{
                    val listing= mutableListOf<MyNoticeItem>()
                    mNoticeData.forEach {
                        if (it.notice_name.toLowerCase(Locale.ENGLISH).contains(p0.toString().toLowerCase(
                                Locale.ENGLISH))){
                            listing.add(it)
                        }
                    }

                    result.count=listing.size
                    result.values=listing
                    return result
                }
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                mNoticeDataFilter= p1!!.values as MutableList<MyNoticeItem>
                notifyDataSetChanged()
            }
        }

    }
}