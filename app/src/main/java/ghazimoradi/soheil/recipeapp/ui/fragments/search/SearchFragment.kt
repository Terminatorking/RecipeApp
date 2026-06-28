package ghazimoradi.soheil.recipeapp.ui.fragments.search

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.databinding.FragmentSearchBinding
import ghazimoradi.soheil.recipeapp.ui.adapters.RecentAdapter
import ghazimoradi.soheil.recipeapp.ui.fragments.recipe.RecipeFragmentDirections.actionToDetail
import ghazimoradi.soheil.recipeapp.utils.base.BaseFragment
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest.Error
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest.Loading
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest.Success
import ghazimoradi.soheil.recipeapp.utils.setupRecyclerview
import ghazimoradi.soheil.recipeapp.utils.showSnackBar
import ghazimoradi.soheil.recipeapp.viewmodels.SearchViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentSearchBinding
        get() = FragmentSearchBinding::inflate

    private val viewModel: SearchViewModel by viewModels()

    @Inject
    lateinit var recentAdapter: RecentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            initInternetLayout(isNetworkAvailable)

            requireActivity().window.decorView.apply {
                viewTreeObserver.addOnGlobalLayoutListener {
                    val rect = Rect()
                    getWindowVisibleDisplayFrame(rect)
                    if (height - rect.bottom <= height * 0.1399)
                        rootMotion.transitionToStart()
                    else
                        rootMotion.transitionToEnd()
                }
            }

            searchEdt.addTextChangedListener {
                if (it.toString().length > 2 && isNetworkAvailable)
                    viewModel.callSearchApi(it.toString())
            }
        }

        loadRecentData()
    }

    private fun loadRecentData() {
        binding.apply {
            viewModel.searchData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Loading -> {
                        searchList.showShimmer()
                    }

                    is Success -> {
                        searchList.hideShimmer()
                        response.data?.let { data ->
                            if (data.results!!.isNotEmpty()) {
                                recentAdapter.setData(data.results)
                                initRecentRecycler()
                            }
                        }
                    }

                    is Error -> {
                        searchList.hideShimmer()
                        binding.root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }

    private fun initRecentRecycler() {
        binding.searchList.setupRecyclerview(
            LinearLayoutManager(requireContext()),
            recentAdapter
        )

        recentAdapter.setOnItemClickListener {
            gotoDetailPage(it)
        }
    }

    private fun gotoDetailPage(id: Int) {
        findNavController().navigate(actionToDetail(id))
    }

    private fun initInternetLayout(isConnected: Boolean) {
        binding.rootMotion.apply {
            constraintSetIds.forEach {
                val constraintSet = getConstraintSet(it) ?: return@forEach
                constraintSet.setVisibility(
                    binding.internetLay.id,
                    if (isConnected) View.GONE else View.VISIBLE
                )
                constraintSet.setVisibility(
                    binding.searchEdt.id,
                    if (isConnected) View.VISIBLE else View.GONE
                )
                constraintSet.applyTo(this)
            }
        }
    }
}