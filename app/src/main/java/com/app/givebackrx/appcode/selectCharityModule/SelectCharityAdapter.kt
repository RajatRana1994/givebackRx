package com.app.givebackrx.appcode.selectCharityModule

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.CharityData
import com.app.givebackrx.listeners.PairListCLickListener
import com.app.givebackrx.utils.extension.onOffVisibility
import com.bumptech.glide.Glide

class SelectCharityAdapter(
    var context: Context,
    val mCharityList: MutableList<CharityData>,
    val listener: PairListCLickListener,
    val isCharityUser: Boolean
) : RecyclerView.Adapter<SelectCharityAdapter.VHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_item_charity, parent, false)
        )
    }

    override fun getItemCount(): Int = mCharityList.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            tvCharityName.text = mCharityList[position].charity_name
            tvDescription.text = mCharityList[position].description
            tvamount.text = mCharityList[position].donation_from_giveback_enterprises
            if (!mCharityList[position].charity_logo.isEmpty()) {
                Glide.with(context).load(mCharityList[position].charity_logo).into(ivCharity);
            }
            textData.text =
                Html.fromHtml("give" + "<font><b>" + "back" + "</b></font>" + "Rx's total donations")
            tvSelectCharity.setOnClickListener {
                listener.onFirstListClick(mCharityList[position], position)
            }
            tvViewMore.setOnClickListener {
                listener.onSecondListClick(mCharityList[position], position)
            }

            if (isCharityUser) {
                tvSelectCharity.onOffVisibility(false)
                tvSelected.onOffVisibility(false)
            } else {
                if (mCharityList[position].selected) {
                    tvSelectCharity.onOffVisibility(false)
                    tvSelected.onOffVisibility(true)
                } else {
                    tvSelectCharity.onOffVisibility(true)
                    tvSelected.onOffVisibility(false)
                }
            }
        }
    }

    class VHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCharityName = view.findViewById<TextView>(R.id.tvCharityName)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        val tvamount = view.findViewById<TextView>(R.id.tvamount)
        val tvViewMore = view.findViewById<TextView>(R.id.tvViewMore)
        val ivCharity = view.findViewById<ImageView>(R.id.ivCharity)
        val textData = view.findViewById<TextView>(R.id.textView19)
        val tvSelectCharity = view.findViewById<Button>(R.id.tvSelectCharity)
        val tvSelected = view.findViewById<TextView>(R.id.tvSelected)
    }
}