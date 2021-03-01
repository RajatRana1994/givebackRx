package com.app.givebackrx.appcode.howitworksModule

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.app.givebackrx.R
import com.bumptech.glide.Glide


class ViewPagerAdapter(private val mContext: Context, private val mListData: ArrayList<Drawable>) :
    PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return mListData.size
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.view_pager, container, false) as ViewGroup

        val imageView = view.findViewById<ImageView>(R.id.imageview)
        Glide.with(view).load(mListData[position]).into(imageView);
//        val button = view.findViewById(R.id.button)
//        button.setText(mListData[position])
//        button.setOnClickListener(object : View.OnClickListener() {
//            fun onClick(view: View) {
//                textView.setText(mListData[position])
//            }
//        })

        container.addView(view)
        return view
    }
}