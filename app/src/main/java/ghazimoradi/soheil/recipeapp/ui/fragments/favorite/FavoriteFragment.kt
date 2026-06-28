package ghazimoradi.soheil.recipeapp.ui.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.databinding.FragmentFavoriteBinding
import ghazimoradi.soheil.recipeapp.ui.adapters.FavoriteAdapter
import ghazimoradi.soheil.recipeapp.ui.fragments.recipe.RecipeFragmentDirections.actionToDetail
import ghazimoradi.soheil.recipeapp.utils.base.BaseFragment
import ghazimoradi.soheil.recipeapp.utils.isVisible
import ghazimoradi.soheil.recipeapp.utils.setupRecyclerview
import ghazimoradi.soheil.recipeapp.viewmodels.FavoriteViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentFavoriteBinding
        get() = FragmentFavoriteBinding::inflate

    private val viewModel: FavoriteViewModel by viewModels()

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel.readFavoriteData.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    emptyTxt.isVisible(false, favoriteList)
                    favoriteAdapter.setData(it)

                    favoriteList.setupRecyclerview(
                        LinearLayoutManager(requireContext()),
                        favoriteAdapter
                    )

                    favoriteAdapter.setOnItemClickListener { id ->
                        findNavController().navigate(actionToDetail(id))
                    }

                } else {
                    emptyTxt.isVisible(true, favoriteList)
                }
            }
        }
    }
}