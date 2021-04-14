package ro.twodoors.exchangerates.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ro.twodoors.exchangerates.R
import ro.twodoors.exchangerates.data.model.Currency
import ro.twodoors.exchangerates.data.repository.Repository
import ro.twodoors.exchangerates.util.CurrencyEvent
import ro.twodoors.exchangerates.util.DispatcherProvider
import ro.twodoors.exchangerates.util.Helper.currencies
import ro.twodoors.exchangerates.util.Helper.getRateForCurrency
import ro.twodoors.exchangerates.util.Resource
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class SharedViewModel @Inject constructor(
        private val repository: Repository,
        private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _currencyFrom = MutableStateFlow(
                    Currency("EUR", "Euro", "€", R.drawable.ic_eur))
    val currencyFrom : StateFlow<Currency> = _currencyFrom

    private val _currencyTo = MutableStateFlow(
                    Currency("USD", "United States Dollar", "$", R.drawable.ic_usd))
    val currencyTo : StateFlow<Currency> = _currencyTo

    private val _amount = MutableStateFlow("0")
    val amount: StateFlow<String> = _amount

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion : StateFlow<CurrencyEvent> = _conversion

    private fun convert(){
        val fromAmount = amount.value.toFloatOrNull()
        if (fromAmount == null) {
            _conversion.value = CurrencyEvent.Failure("0")
            return
        }

        viewModelScope.launch (dispatchers.io){
            _conversion.value = CurrencyEvent.Loading

            when(val ratesResponse = repository.getRates(currencyFrom.value.code)) {
                is Resource.Error -> _conversion.value = CurrencyEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.rates
                    val date = ratesResponse.data.date
                    val rate =  getRateForCurrency(currencyTo.value.code, rates)
                    if(rate == null) {
                        _conversion.value = CurrencyEvent.Failure("Unexpected error")
                    } else {
                        val convertedCurrency = round(fromAmount * rate * 100) / 100
                        _conversion.value = CurrencyEvent.Success(
                                "$convertedCurrency",
                                date = "Exchange rates provided by the European Central Bank on $date",
                                rate = "1 ${currencyFrom.value.code} = $rate ${currencyTo.value.code}"
                        )
                    }
                }
            }
        }
    }

    fun switchCurrencies(){
        val tempCurrency = currencyFrom.value
        _currencyFrom.value = currencyTo.value
        _currencyTo.value = tempCurrency

        convert()
    }

    fun setSelectedFromCurrency(currency: Currency){
        _currencyFrom.value = currency
    }

    fun setSelectedToCurrency(currency: Currency) {
        _currencyTo.value = currency
    }

    fun filter(query : String) : MutableList<Currency>{
        val filteredList = mutableListOf<Currency>()
        for (currency in currencies){
            if (currency.code.toLowerCase().contains(query.toLowerCase()) ||
                    currency.description.toLowerCase().contains(query.toLowerCase())){
                filteredList.add(currency)
            }
        }
        return filteredList
    }

    fun updateAmount(amount: String) {
        _amount.value = amount
        convert()
    }

}