package ro.twodoors.exchangerates.util

import ro.twodoors.exchangerates.R
import ro.twodoors.exchangerates.data.model.Currency
import ro.twodoors.exchangerates.data.model.Rates

object Helper {

    const val SOURCE_FROM = "from"
    const val SOURCE_TO = "to"

    val currencies: MutableList<Currency> = mutableListOf(
            Currency("AUD", "Australian Dollar", "A$", R.drawable.ic_aud),
            Currency("BGN", "Bulgarian Lev", "Лв.", R.drawable.ic_bgn),
            Currency("BRL", "Brazilian Real", "R$", R.drawable.ic_brl),
            Currency("CAD", "Canadian Dollar", "CA$", R.drawable.ic_cad),
            Currency("CHF", "Swiss Franc", "CHf", R.drawable.ic_chf),
            Currency("CNY", "Chinese Yuan", "¥", R.drawable.ic_cny),
            Currency("CZK", "Czech Koruna", "Kč", R.drawable.ic_czk),
            Currency("DKK", "Danish Krone", "kr", R.drawable.ic_dkk),
            Currency("EUR", "Euro", "€", R.drawable.ic_eur),
            Currency("GBP", "Pound", "£", R.drawable.ic_gbp),
            Currency("HKD", "Hong Kong Dollar", "HK$", R.drawable.ic_hkd),
            Currency("HRK", "Croatian Kuna", "kn", R.drawable.ic_hrk),
            Currency("HUF", "Hungarian Forint", "Ft", R.drawable.ic_huf),
            Currency("IDR", "Indonesian Rupiah", "Rp", R.drawable.ic_idr),
            Currency("ILS", "Israeli Shekel", "₪", R.drawable.ic_ils),
            Currency("INR", "Indian Rupee", "₹", R.drawable.ic_inr),
            Currency("ISK", "Icelandic króna", "kr", R.drawable.ic_isk),
            Currency("JPY", "Japanese Yen", "¥", R.drawable.ic_jpy),
            Currency("KRW", "South Korean Won", "₩", R.drawable.ic_krw),
            Currency("MXN", "Mexican Peso", "Mex$", R.drawable.ic_mxn),
            Currency("MYR", "Malaysian Ringgit", "RM", R.drawable.ic_myr),
            Currency("NOK", "Norwegian Krone", "kr", R.drawable.ic_nok),
            Currency("NZD", "New Zealand Dollar", "$", R.drawable.ic_nzd),
            Currency("PHP", "Philippine Peso", "₱", R.drawable.ic_php),
            Currency("PLN", "Polish Złoty", "zł", R.drawable.ic_pln),
            Currency("RON", "Romanian Leu", "lei", R.drawable.ic_ron),
            Currency("RUB", "Russian ruble", "₽", R.drawable.ic_rub),
            Currency("SEK", "Swedish Krona", "kr", R.drawable.ic_sek),
            Currency("SGD", "Singapore Dollar", "S$", R.drawable.ic_sgd),
            Currency("THB", "Thai Baht", "฿", R.drawable.ic_thb),
            Currency("TRY", "Turkish Lira", "₺", R.drawable.ic_try),
            Currency("USD", "United States Dollar", "$", R.drawable.ic_usd),
            Currency("ZAR", "South African Rand", "R", R.drawable.ic_zar)
    )

    fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
        "CAD" -> rates.cAD
        "HKD" -> rates.hKD
        "ISK" -> rates.iSK
        "EUR" -> rates.eUR
        "PHP" -> rates.pHP
        "DKK" -> rates.dKK
        "HUF" -> rates.hUF
        "CZK" -> rates.cZK
        "AUD" -> rates.aUD
        "RON" -> rates.rON
        "SEK" -> rates.sEK
        "IDR" -> rates.iDR
        "INR" -> rates.iNR
        "BRL" -> rates.bRL
        "RUB" -> rates.rUB
        "HRK" -> rates.hRK
        "JPY" -> rates.jPY
        "THB" -> rates.tHB
        "CHF" -> rates.cHF
        "SGD" -> rates.sGD
        "PLN" -> rates.pLN
        "BGN" -> rates.bGN
        "CNY" -> rates.cNY
        "NOK" -> rates.nOK
        "NZD" -> rates.nZD
        "ZAR" -> rates.zAR
        "USD" -> rates.uSD
        "MXN" -> rates.mXN
        "ILS" -> rates.iLS
        "GBP" -> rates.gBP
        "KRW" -> rates.kRW
        "MYR" -> rates.mYR
        "TRY" -> rates.tRY
        else -> null
    }
}