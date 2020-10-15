package com.altun.currencyapp_sweeftdigital

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.currency_recycler_view_item_layout.view.*

class CurrencyRecyclerViewAdapter(private val items: MutableList<CurrencyModel>) :
    RecyclerView.Adapter<CurrencyRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.currency_recycler_view_item_layout, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var model: CurrencyModel

        fun onBind() {
            model = items[adapterPosition]
            itemView.currencyNameTextView.text = model.name
            itemView.currencyFullNameTextView.text = model.fullName
            itemView.currencyRateTextView.text = model.rate
            itemView.currencyDiffTextView.text = model.diff
            Glide.with(itemView.context).load(model.img).into(itemView.currencyDiffImageView)
            itemView.setOnClickListener {
                val arr = model.fullName!!.split(" ")[0].toInt()
                Tools.showCurrencyCalculateDialog(itemView.context,model.name!!,model.rate!!.toFloat()/arr)
            }
        }
    }


}