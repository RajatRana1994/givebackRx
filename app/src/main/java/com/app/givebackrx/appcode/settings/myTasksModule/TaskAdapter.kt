package com.app.givebackrx.appcode.settings.myTasksModule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.MyTaskItem
import com.app.givebackrx.data.entity.PharmacyData
import com.app.givebackrx.listeners.SingleListCLickListener
import java.util.*

class TaskAdapter(
    var mTasksDataFiltered: MutableList<MyTaskItem>,
    val listener: SingleListCLickListener) : RecyclerView.Adapter<TaskAdapter.VHolder>(),Filterable {

    val mTasksData=mTasksDataFiltered

    var visiblePos=-1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_items_tasks,parent,false))
    }

    override fun getItemCount(): Int = mTasksDataFiltered.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            tvName.text=mTasksDataFiltered[position].task_name
            tvAssignDate.text=mTasksDataFiltered[position].assigned_on
            tvDueDate.text=mTasksDataFiltered[position].due_date
            tvTaskDesc.text=mTasksDataFiltered[position].description
            tvMarkComplete.text=if (mTasksDataFiltered[position].task_status.equals("Task Completed")) mTasksDataFiltered[position].task_status else "Mark As Complete"
//            tvMarkComplete.setTextColor(ContextCompat.getColor(tvMarkComplete.context,if(mTasksDataFiltered[position].task_status.equals("Mark As Complete")) R.color.blue_color else if(mTasksDataFiltered[position].task_status.equals("Task Completed")) R.color.green_color else  R.color.pink_color))
            tvMarkComplete.setTextColor(ContextCompat.getColor(tvMarkComplete.context, if(mTasksDataFiltered[position].task_status.equals("Task Completed")) R.color.green_color else  R.color.blue_color))

            tvMarkComplete.setOnClickListener {
                if(mTasksDataFiltered[position].task_status.equals("Task Completed").not()){
                    listener.onSingleListClick(mTasksDataFiltered[position],position)
                }
            }
        }
    }

    class VHolder(val view: View):RecyclerView.ViewHolder(view){
        val tvName=view.findViewById<TextView>(R.id.tvName)
        val tvAssignDate=view.findViewById<TextView>(R.id.tvAssignDate)
        val tvDueDate=view.findViewById<TextView>(R.id.tvDueDate)
        val tvTaskDesc=view.findViewById<TextView>(R.id.tvTaskDesc)
        val tvMarkComplete=view.findViewById<TextView>(R.id.tvMarkComplete)
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
                    result.count=mTasksData.size
                    result.values=mTasksData
                    return result
                }else{
                    val listing= mutableListOf<MyTaskItem>()
                    mTasksData.forEach {
                        if (it.task_name.toLowerCase(Locale.ENGLISH).contains(p0.toString().toLowerCase(Locale.ENGLISH))){
                            listing.add(it)
                        }
                    }

                    result.count=listing.size
                    result.values=listing
                    return result
                }
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                mTasksDataFiltered= p1!!.values as MutableList<MyTaskItem>
                notifyDataSetChanged()
            }
        }
    }
}