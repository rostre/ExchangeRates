package ro.twodoors.exchangerates.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ro.twodoors.exchangerates.R
import ro.twodoors.exchangerates.data.model.Currency
import ro.twodoors.exchangerates.databinding.FragmentHomeBinding
import ro.twodoors.exchangerates.util.Helper.SOURCE_FROM
import ro.twodoors.exchangerates.util.Helper.SOURCE_TO

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        binding.etFromAmount.doOnTextChanged { text, _, _, _ ->
            viewModel.updateAmount(text.toString())
            //viewModel.convert(text.toString())
        }

        binding.btnConvert.setOnClickListener {
            viewModel.switchCurrencies()
        }

        binding.fromCurrency.ivNavigate.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSelectCurrencyFragment(SOURCE_FROM))
        }

        binding.toCurrency.ivNavigate.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSelectCurrencyFragment(SOURCE_TO))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.currencyFrom.collect { event ->
                event.getContentIfNotHandled().let { currency ->
                    if (currency != null){
                        binding.fromCurrency.tvCurrencyCode.text = currency.code
                        binding.fromCurrency.tvCurrencyName.text = currency.description
                        binding.fromCurrency.ivFlagIcon.setImageResource(currency.icon)
                        binding.tvCurrencySymbol.text = currency.symbol
                    } else {
                        binding.fromCurrency.tvCurrencyCode.text = event.peekContent().code
                        binding.fromCurrency.tvCurrencyName.text = event.peekContent().description
                        binding.fromCurrency.ivFlagIcon.setImageResource(event.peekContent().icon)
                        binding.tvCurrencySymbol.text = event.peekContent().symbol
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.currencyTo.collect { event ->
                event.getContentIfNotHandled().let { currency ->
                    if (currency != null){
                        binding.toCurrency.tvCurrencyCode.text = currency.code
                        binding.toCurrency.tvCurrencyName.text = currency.description
                        binding.toCurrency.ivFlagIcon.setImageResource(currency.icon)
                        binding.tvToCurrencySymbol.text = currency.symbol
                    } else {
                        binding.toCurrency.tvCurrencyCode.text = event.peekContent().code
                        binding.toCurrency.tvCurrencyName.text = event.peekContent().description
                        binding.toCurrency.ivFlagIcon.setImageResource(event.peekContent().icon)
                        binding.tvToCurrencySymbol.text = event.peekContent().symbol
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { event ->
                when(event) {
                    is MainViewModel.CurrencyEvent.Success -> {
                        binding.progressBar.isVisible = false
                        binding.tvResult.setTextColor(Color.BLACK)
                        binding.tvResult.text = event.resultText
                        binding.tvDate.text = event.date
                        binding.tvRate.text = event.rate
                    }
                    is MainViewModel.CurrencyEvent.Failure -> {
                        binding.progressBar.isVisible = false
                        binding.tvResult.text = event.errorText
                        binding.tvDate.text = ""
                        binding.tvRate.text = ""
                    }
                    is MainViewModel.CurrencyEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}