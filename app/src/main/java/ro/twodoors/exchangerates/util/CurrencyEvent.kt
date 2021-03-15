package ro.twodoors.exchangerates.util

sealed class CurrencyEvent {
    class Success(val resultText: String, val date : String, val rate : String): CurrencyEvent()
    class Failure(val errorText: String): CurrencyEvent()
    object Loading : CurrencyEvent()
    object Empty : CurrencyEvent()
}