package ghazimoradi.soheil.recipeapp.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import ghazimoradi.soheil.recipeapp.viewmodels.CheckInternetViewModel
import kotlinx.coroutines.launch

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    protected abstract val bindingInflater: (inflater: LayoutInflater) -> T

    private var _binding: T? = null
    protected val binding: T get() = requireNotNull(_binding)

    protected var isNetworkAvailable = true

    private val checkInternetViewModel: CheckInternetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            checkInternetViewModel.isNetworkAvailable.collect {
                isNetworkAvailable = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}