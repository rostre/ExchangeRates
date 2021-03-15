package ro.twodoors.exchangerates.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import ro.twodoors.exchangerates.R
import ro.twodoors.exchangerates.data.model.Currency
import ro.twodoors.exchangerates.databinding.FragmentSelectCurrencyBinding
import ro.twodoors.exchangerates.util.Helper.SOURCE_FROM
import ro.twodoors.exchangerates.util.Helper.SOURCE_TO
import ro.twodoors.exchangerates.util.Helper.currencies

@AndroidEntryPoint
class SelectCurrencyFragment : Fragment(R.layout.fragment_select_currency) {

    private var _binding: FragmentSelectCurrencyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedViewModel by activityViewModels()
    private val args : SelectCurrencyFragmentArgs by navArgs()
    private val onCLick : (Currency) -> Unit = {currency -> adapterOnClick(currency) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSelectCurrencyBinding.bind(view)
        setToolbarTitle()

        val currencyAdapter = CurrencyAdapter(currencies, onCLick)
        binding.rvCurrencies.apply {
            adapter = currencyAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }

        binding.searchCurrency.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().toLowerCase()
            val filteredList = viewModel.filter(query)
            currencyAdapter.filter(filteredList)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setToolbarTitle() {
        when(args.source){
            SOURCE_FROM -> {
                binding.toolbar.title = getString(R.string.select_from_currency)
            }

            SOURCE_TO -> {
                binding.toolbar.title = getString(R.string.select_to_currency)
            }

            else -> {
                binding.toolbar.title = getString(R.string.select_currency)
            }
        }
    }

    private fun adapterOnClick(currency : Currency) {
        if (args.source == SOURCE_FROM){
            viewModel.setSelectedFromCurrency(currency)
        }
        if (args.source == SOURCE_TO){
            viewModel.setSelectedToCurrency(currency)
        }
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}