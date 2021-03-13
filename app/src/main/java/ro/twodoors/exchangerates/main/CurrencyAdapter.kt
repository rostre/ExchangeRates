package ro.twodoors.exchangerates.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import ro.twodoors.exchangerates.data.model.Currency
import ro.twodoors.exchangerates.databinding.CurrencyListItemBinding
import ro.twodoors.exchangerates.util.Helper.currencies

class CurrencyAdapter(var currencies : MutableList<Currency>,
        val adapterOnClick : (View, Currency) -> Unit
): RecyclerView.Adapter<CurrencyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = CurrencyListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = currencies[position]
        holder.bind(currency, adapterOnClick)
    }

    override fun getItemCount() = currencies.size

    fun filter(filteredList : MutableList<Currency>){
        currencies = filteredList
        notifyDataSetChanged()
    }

}