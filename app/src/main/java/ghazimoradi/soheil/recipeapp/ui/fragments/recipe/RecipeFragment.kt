package ghazimoradi.soheil.recipeapp.ui.fragments.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.R.string.hello
import ghazimoradi.soheil.recipeapp.data.models.recipe.ResponseRecipes.Result
import ghazimoradi.soheil.recipeapp.databinding.FragmentRecipeBinding
import ghazimoradi.soheil.recipeapp.ui.adapters.PopularAdapter
import ghazimoradi.soheil.recipeapp.ui.adapters.RecentAdapter
import ghazimoradi.soheil.recipeapp.ui.fragments.recipe.RecipeFragmentDirections.actionToDetail
import ghazimoradi.soheil.recipeapp.utils.DELAY_TIME
import ghazimoradi.soheil.recipeapp.utils.REPEAT_TIME
import ghazimoradi.soheil.recipeapp.utils.base.BaseFragment
import ghazimoradi.soheil.recipeapp.utils.delay
import ghazimoradi.soheil.recipeapp.utils.doWorkOnLifecycleScope
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest.Error
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest.Loading
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest.Success
import ghazimoradi.soheil.recipeapp.utils.onceObserve
import ghazimoradi.soheil.recipeapp.utils.setupRecyclerview
import ghazimoradi.soheil.recipeapp.utils.showSnackBar
import ghazimoradi.soheil.recipeapp.viewmodels.RecipeViewModel
import ghazimoradi.soheil.recipeapp.viewmodels.RegisterViewModel
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class RecipeFragment : BaseFragment<FragmentRecipeBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentRecipeBinding
        get() = FragmentRecipeBinding::inflate

    private var autoScrollIndex = 0

    private val registerViewModel: RegisterViewModel by viewModels()
    private val recipeViewModel: RecipeViewModel by viewModels()

    private val args: RecipeFragmentArgs by navArgs()

    @Inject
    lateinit var popularAdapter: PopularAdapter

    @Inject
    lateinit var recentAdapter: RecentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showUsername()

        callPopularData()
        callRecentData()

        loadPopularData()
        loadRecentData()
    }

    private fun callPopularData() {
        initPopularRecycler()
        recipeViewModel.readPopularFromDb.onceObserve(viewLifecycleOwner) { populars ->
            if (populars.isNotEmpty()) {
                populars[0].response.results?.let { result ->
                    setupLoading(false, binding.popularList)
                    fillPopularAdapter(result.toMutableList())
                }
            } else {
                recipeViewModel.callPopularApi()
            }
        }
    }

    private fun callRecentData() {
        initRecentRecycler()
        recipeViewModel.readRecentFromDb.onceObserve(viewLifecycleOwner) { recents ->
            if (recents.isNotEmpty() && recents.size > 1 && !args.isUpdateData) {
                recents[1].response.results?.let { results ->
                    setupLoading(false, binding.recipesList)
                    recentAdapter.setData(results)
                }
            } else {
                recipeViewModel.callRecentApi()
            }
        }
    }

    fun showUsername() {
        doWorkOnLifecycleScope {
            registerViewModel.readData.collect {
                binding.usernameTxt.text =
                    "${getString(hello)}, ${it.username} ${getEmojiByUnicode()}"
            }
        }
    }

    private fun getEmojiByUnicode(): String {
        return String(Character.toChars(0x1f44b))
    }

    private fun loadPopularData() {
        binding.apply {
            recipeViewModel.popularData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Loading -> {
                        setupLoading(true, popularList)
                    }

                    is Success -> {
                        setupLoading(false, popularList)
                        response.data?.results?.let { results ->
                            if (results.isNotEmpty()) {
                                fillPopularAdapter(results.toMutableList())
                            }
                        }
                    }

                    is Error -> {
                        setupLoading(false, popularList)
                        binding.root.showSnackBar(response.message.toString())
                    }
                }
            }
        }
    }

    private fun loadRecentData() {
        binding.apply {
            recipeViewModel.recentData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Loading -> {
                        setupLoading(true, recipesList)
                    }

                    is Success -> {
                        setupLoading(false, recipesList)
                        response.data?.results?.let { results ->
                            if (results.isNotEmpty()) {
                                recentAdapter.setData(results)
                            }
                        }
                    }

                    is Error -> {
                        setupLoading(false, recipesList)
                        root.showSnackBar(response.message.toString())
                    }
                }
            }
        }
    }

    private fun setupLoading(isShownLoading: Boolean, shimmer: ShimmerRecyclerView) {
        shimmer.apply {
            if (isShownLoading) showShimmer() else hideShimmer()
        }
    }

    private fun fillPopularAdapter(result: MutableList<Result>) {
        popularAdapter.setData(result)
        autoScrollPopular(result)
    }

    private fun autoScrollPopular(list: List<Result>) {
        doWorkOnLifecycleScope {
            repeat(REPEAT_TIME) {
                DELAY_TIME.delay()
                if (autoScrollIndex < list.size) {
                    autoScrollIndex += 1
                } else {
                    autoScrollIndex = 0
                }
                binding.popularList.smoothScrollToPosition(autoScrollIndex)
            }
        }
    }

    private fun initPopularRecycler() {
        val snapHelper = LinearSnapHelper()

        binding.popularList.setupRecyclerview(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false,
            ),
            popularAdapter
        )

        snapHelper.attachToRecyclerView(binding.popularList)

        popularAdapter.setOnItemClickListener {
            gotoDetailPage(it)
        }
    }

    private fun initRecentRecycler() {
        binding.recipesList.setupRecyclerview(
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
}