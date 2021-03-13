package ro.twodoors.exchangerates.main

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ro.twodoors.exchangerates.data.model.Currency
import ro.twodoors.exchangerates.databinding.CurrencyListItemBinding

class CurrencyViewHolder(
    val binding : CurrencyListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(currency : Currency, adapterOnClick : (View, Currency) -> Unit){
            binding.apply {
                ivFlagIcon.setImageResource(currency.icon)
                tvCurrencyCode.text = currency.code
                tvCurrencyName.text = currency.description
                itemView.setOnClickListener { adapterOnClick(it, currency) }
            }
        }
}