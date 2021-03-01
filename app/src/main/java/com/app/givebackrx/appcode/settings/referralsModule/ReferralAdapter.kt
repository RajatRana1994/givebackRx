package com.app.givebackrx.appcode.settings.referralsModule

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
import com.app.givebackrx.data.entity.ReferralItem
import com.app.givebackrx.listeners.SingleListCLickListener
import com.app.givebackrx.utils.extension.onOffVisibility
import java.util.*

class ReferralAdapter(
    var mReferralDataFilter: MutableList<ReferralItem>,
    val listener: SingleListCLickListener) : RecyclerView.Adapter<ReferralAdapter.VHolder>(),Filterable {

    val mReferralData=mReferralDataFilter
    var visiblePos=-1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_items_referrals,parent,false))
    }

    override fun getItemCount(): Int = mReferralDataFilter.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            tvName.text=mReferralDataFilter[position].name
            tvEmail.text=mReferralDataFilter[position].email
            tvNumber.text=mReferralDataFilter[position].phone
            tvReferBy.text=mReferralDataFilter[position].referred_by
            tvDate.text=mReferralDataFilter[position].referral_send_on
            tvStatus.text=mReferralDataFilter[position].status
            tvStatus.setTextColor(ContextCompat.getColor(tvStatus.context,if (mReferralDataFilter[position].status.equals("Rejected")) R.color.colorPrimary else if (mReferralDataFilter[position].status.equals("Pending")) R.color.light_gray_color else R.color.pink_color))
            tvResend.onOffVisibility(mReferralDataFilter[position].resend_button)

            tvResend.setOnClickListener {
                listener.onSingleListClick(mReferralDataFilter[position],position)
            }
        }
    }

    class VHolder(val view: View):RecyclerView.ViewHolder(view){
        val tvName=view.findViewById<TextView>(R.id.tvName)
        val tvEmail=view.findViewById<TextView>(R.id.tvEmail)
        val tvNumber=view.findViewById<TextView>(R.id.tvNumber)
        val tvReferBy=view.findViewById<TextView>(R.id.tvReferBy)
        val tvDate=view.findViewById<TextView>(R.id.tvDate)
        val tvStatus=view.findViewById<TextView>(R.id.tvStatus)
        val tvResend=view.findViewById<TextView>(R.id.tvResend)
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
                    result.count=mReferralData.size
                    result.values=mReferralData
                    return result
                }else{
                    val listing= mutableListOf<ReferralItem>()
                    mReferralData.forEach {
                        if (it.name.toLowerCase(Locale.ENGLISH).contains(p0.toString().toLowerCase(
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
                mReferralDataFilter= p1!!.values as MutableList<ReferralItem>
                notifyDataSetChanged()
            }
        }

    }
}