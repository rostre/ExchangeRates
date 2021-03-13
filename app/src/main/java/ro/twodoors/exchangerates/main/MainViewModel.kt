package ro.twodoors.exchangerates.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ro.twodoors.exchangerates.R
import ro.twodoors.exchangerates.data.model.Currency
import ro.twodoors.exchangerates.util.DispatcherProvider
import ro.twodoors.exchangerates.util.Event
import ro.twodoors.exchangerates.util.Helper.currencies
import ro.twodoors.exchangerates.util.Helper.getRateForCurrency
import ro.twodoors.exchangerates.util.Resource
import javax.inject.Inject
import kotlin.math.round

const val STATE_FROM_CURRENCY_CODE = "currency_from"
const val STATE_TO_CURRENCY_CODE = "currency_to"
const val STATE_FROM_AMOUNT = "amount"

@HiltViewModel
class MainViewModel @Inject constructor(
        private val repository: MainRepository,
        private val dispatchers: DispatcherProvider,
        private val savedStateHandle : SavedStateHandle
) : ViewModel() {

    sealed class CurrencyEvent {
        class Success(val resultText: String, val date : String, val rate : String): CurrencyEvent()
        class Failure(val errorText: String): CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    init {
        savedStateHandle.get<String>(STATE_FROM_CURRENCY_CODE)?.let { from ->
            setSelectedFromCurrency(currencies.first {
                it.code == from
            })
        }
        savedStateHandle.get<String>(STATE_TO_CURRENCY_CODE)?.let { to ->
            setSelectedToCurrency(currencies.first {
                it.code == to
            })
        }
        savedStateHandle.get<String>(STATE_FROM_AMOUNT)?.let { amount ->
            updateAmount(amount)
        }
    }

    private val _currencyFrom = MutableStateFlow(
            Event(
                    Currency("EUR", "Euro", "€", R.drawable.ic_eur)))
    val currencyFrom : StateFlow<Event<Currency>> = _currencyFrom

    private val _currencyTo = MutableStateFlow(
            Event(
                    Currency("USD", "United States Dollar", "$", R.drawable.ic_usd)))
    val currencyTo : StateFlow<Event<Currency>> = _currencyTo

    private val _amount = MutableStateFlow(Event("1"))
    val amount: StateFlow<Event<String>> = _amount

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion : StateFlow<CurrencyEvent> = _conversion

    fun convert(){
        val fromAmount = amount.value.peekContent().toFloatOrNull()
        if (fromAmount == null) {
            _conversion.value = CurrencyEvent.Failure("0")
            return
        }

        viewModelScope.launch (dispatchers.io){
            _conversion.value = CurrencyEvent.Loading

            when(val ratesResponse = repository.getRates(currencyFrom.value.peekContent().code)) {
                is Resource.Error -> _conversion.value = CurrencyEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.rates
                    val date = ratesResponse.data.date
                    val rate = getRateForCurrency(currencyTo.value.peekContent().code, rates)
                    if(rate == null) {
                        _conversion.value = CurrencyEvent.Failure("Unexpected error")
                    } else {
                        val convertedCurrency = round(fromAmount * rate * 100) / 100
                        _conversion.value = CurrencyEvent.Success(
                            //"$fromAmount ${currencyFrom.value.peekContent().code} = $convertedCurrency ${currencyTo.value.peekContent().code}",
                                "$convertedCurrency",
                                date = date,
                                rate = "1 ${currencyFrom.value.peekContent().code} = $rate ${currencyTo.value.peekContent().code}"
                        )
                    }
                }
            }
        }
    }

    fun switchCurrencies(){
        val tempCurrency = currencyFrom.value.peekContent()
        _currencyFrom.value = Event(currencyTo.value.peekContent())
        _currencyTo.value = Event(tempCurrency)

        convert()
        //convert(amount.value.peekContent())
    }

    fun setSelectedFromCurrency(currency: Currency){
        _currencyFrom.value = Event(currency)
        savedStateHandle.set(STATE_FROM_CURRENCY_CODE, currency.code)
        //convert()
    }

    fun setSelectedToCurrency(currency: Currency) {
        _currencyTo.value = Event(currency)
        savedStateHandle.set(STATE_TO_CURRENCY_CODE, currency.code)

        //convert()
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
        _amount.value = Event(amount)
        savedStateHandle.set(STATE_FROM_AMOUNT, amount)
        convert()
    }

}