package com.app.givebackrx.appcode.printedcard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.PrintedCharity
import com.app.givebackrx.utils.extension.loadOrigImage

class CharityPrintedAdapter(val ctx:Context,val cardsList:MutableList<PrintedCharity>):ArrayAdapter<PrintedCharity>(ctx,android.R.layout.simple_list_item_1){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view :View?=null
        if (convertView==null)
            view =LayoutInflater.from(ctx).inflate(R.layout.row_printed_card_charity,parent,false)
        else view=convertView

        view!!.findViewById<TextView>(R.id.tvCharity).text=cardsList[position].charity_name
        view!!.findViewById<ImageView>(R.id.ivCharity).loadOrigImage(cardsList[position].image_url)


        return view!!
    }

    override fun getCount(): Int {
        return cardsList.size
    }
}