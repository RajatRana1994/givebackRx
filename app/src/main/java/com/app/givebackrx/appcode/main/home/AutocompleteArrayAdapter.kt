package com.app.givebackrx.appcode.main.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.app.givebackrx.R
import com.app.givebackrx.appcode.main.MainActivity
import com.app.givebackrx.data.entity.SearchDrugData

class AutocompleteArrayAdapter(
    var mContext: Context,
    var layoutResourceId: Int,
    val data: List<String>
) : ArrayAdapter<String?>(mContext, layoutResourceId, data!!) {
    val TAG = "AutocompleteCustomArrayAdapter.java"


    override fun getCount(): Int {
        return data.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        try {
            if (convertView == null) {
                // inflate the layout
                convertView = LayoutInflater.from(context).inflate(layoutResourceId, parent, false)
            }

            val textViewItem =
                convertView!!.findViewById<View>(android.R.id.text1) as TextView
            textViewItem.text = data[position]
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return convertView!!
    }



}