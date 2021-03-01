package com.app.givebackrx.appcode.settings.help

import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.appcode.main.pharmacy.ViewAnimationUtils
import com.app.givebackrx.data.entity.FaqItem
import com.app.givebackrx.listeners.SingleListCLickListener
import com.app.givebackrx.utils.extension.onOffVisibility

class HelpAdapter(
    val mhelpList: MutableList<FaqItem>,
    val listener: SingleListCLickListener) : RecyclerView.Adapter<HelpAdapter.VHolder>() {

    var visiblePos=-1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_help_item,parent,false))
    }

    override fun getItemCount(): Int = mhelpList.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            tvQues.text=Html.fromHtml(mhelpList[position].question)
            tvAns.text=Html.fromHtml(mhelpList[position].answer)

            if (mhelpList[position].visible) {
                ViewAnimationUtils.expand(tvAns)
                expandAction.rotation = 90f
            }else{
                tvAns.onOffVisibility(false)
                expandAction.rotation=270f
            }
            expandAction.setOnClickListener {
                if (visiblePos!=-1 && visiblePos!=position){
                    mhelpList[visiblePos].visible=false
                    notifyItemChanged(visiblePos)
                }
                visiblePos=position
                mhelpList[position].visible=!mhelpList[position].visible
                notifyItemChanged(position)
            }
            tvAns.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    class VHolder(val view: View):RecyclerView.ViewHolder(view){
        val expandAction=view.findViewById<ImageView>(R.id.expandAction)
        val tvQues=view.findViewById<TextView>(R.id.tvQues)
        val tvAns=view.findViewById<TextView>(R.id.tvAns)
    }

}