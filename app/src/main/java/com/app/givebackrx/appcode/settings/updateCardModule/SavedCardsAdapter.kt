package com.app.givebackrx.appcode.settings.updateCardModule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.givebackrx.R
import com.app.givebackrx.appcode.payment.CardValidator
import com.app.givebackrx.data.entity.SavedCard
import com.app.givebackrx.data.entity.SavedCards
import com.app.givebackrx.listeners.SingleListCLickListener
import kotlinx.android.synthetic.main.fragment_payment.*

class SavedCardsAdapter(
    var mmSavedCards: MutableList<SavedCards>,
    val listener: SingleListCLickListener
) : RecyclerView.Adapter<SavedCardsAdapter.VHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_saved_cards, parent, false)
        )
    }

    override fun getItemCount(): Int = mmSavedCards.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.apply {
            cardTypeName.text=mmSavedCards[position].card_type
            mCardNum.text=mmSavedCards[position].card_number
            ivDelete.setOnClickListener { listener.onSingleListClick("delete",position) }
        }
    }

    class VHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val cardTypeName = view.findViewById<TextView>(R.id.cardTypeName)
        val ivDelete = view.findViewById<ImageView>(R.id.ivDelete)
        val mCardNum = view.findViewById<TextView>(R.id.mCardNum)
    }

}