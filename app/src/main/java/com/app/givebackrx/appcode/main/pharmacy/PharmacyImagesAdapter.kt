package com.app.givebackrx.appcode.main.pharmacy

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.data.entity.DrugImages
import com.app.givebackrx.data.preferences.PreferenceHelper
import com.app.givebackrx.listeners.SingleListCLickListener
import com.app.givebackrx.utils.extension.loadImageRadius
import kotlinx.android.synthetic.main.fragment_pharmacy.*
import java.util.*

class PharmacyImagesAdapter(
    val mPharmacyData: MutableList<DrugImages>,
    val listener: SingleListCLickListener,
    val mPrefs: PreferenceHelper
) : RecyclerView.Adapter<PharmacyImagesAdapter.VHolder>() {

    var visiblePos = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_item_iamges, parent, false)
        )
    }

    override fun getItemCount(): Int = mPharmacyData.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            mIvImage.loadImageRadius(mPharmacyData[position].drug_image_url)
            mTvName.text = mPharmacyData[position].drug_image_name
            }

        }


    class VHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val mIvImage = view.findViewById<ImageView>(R.id.mIvImage)
        val mTvName = view.findViewById<TextView>(R.id.mTvName)

    }


}