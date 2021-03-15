package ro.twodoors.exchangerates.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import ro.twodoors.exchangerates.R
import ro.twodoors.exchangerates.databinding.FragmentHomeBinding
import ro.twodoors.exchangerates.util.CurrencyEvent
import ro.twodoors.exchangerates.util.Helper.SOURCE_FROM
import ro.twodoors.exchangerates.util.Helper.SOURCE_TO

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        binding.etFromAmount.doOnTextChanged { text, _, _, _ ->
            viewModel.updateAmount(text.toString())
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
                        binding.apply {
                            fromCurrency.apply {
                                tvCurrencyCode.text = currency.code
                                tvCurrencyName.text = currency.description
                                ivFlagIcon.setImageResource(currency.icon)
                            }
                            tvCurrencySymbol.text = currency.symbol
                        }
                    } else {
                        binding.apply {
                            fromCurrency.apply {
                                tvCurrencyCode.text = event.peekContent().code
                                tvCurrencyName.text = event.peekContent().description
                                ivFlagIcon.setImageResource(event.peekContent().icon)
                            }
                            tvCurrencySymbol.text = event.peekContent().symbol
                        }
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.currencyTo.collect { event ->
                event.getContentIfNotHandled().let { currency ->
                    if (currency != null){
                        binding.apply {
                            toCurrency.apply {
                                tvCurrencyCode.text = currency.code
                                tvCurrencyName.text = currency.description
                                ivFlagIcon.setImageResource(currency.icon)
                            }
                            tvToCurrencySymbol.text = currency.symbol
                        }
                    } else {
                        binding.apply {
                            toCurrency.apply {
                                tvCurrencyCode.text = event.peekContent().code
                                tvCurrencyName.text = event.peekContent().description
                                ivFlagIcon.setImageResource(event.peekContent().icon)
                            }
                            tvToCurrencySymbol.text = event.peekContent().symbol
                        }
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { event ->
                when(event) {

                    is CurrencyEvent.Success -> {
                        binding.apply {
                            progressBar.isVisible = false
                            tvResult.text = event.resultText
                            tvResult.setTextColor(Color.BLACK)
                            tvDate.text = event.date
                            tvRate.text = event.rate
                        }
                    }
                    is CurrencyEvent.Failure -> {
                        binding.apply {
                            progressBar.isVisible = false
                            tvResult.text = event.errorText
                            tvDate.text = ""
                            tvRate.text = ""
                        }
                    }
                    is CurrencyEvent.Loading -> {
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