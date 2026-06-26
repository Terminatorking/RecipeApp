package ghazimoradi.soheil.recipeapp.ui.fragments.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import coil3.load
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.BuildConfig.VERSION_NAME
import ghazimoradi.soheil.recipeapp.R.drawable.bg_splash
import ghazimoradi.soheil.recipeapp.R.string.version
import ghazimoradi.soheil.recipeapp.R.id.splashFragment
import ghazimoradi.soheil.recipeapp.R.id.actionToRecipe
import ghazimoradi.soheil.recipeapp.R.id.actionToRegister
import ghazimoradi.soheil.recipeapp.databinding.FragmentSplashBinding
import ghazimoradi.soheil.recipeapp.utils.base.BaseFragment
import ghazimoradi.soheil.recipeapp.utils.delay
import ghazimoradi.soheil.recipeapp.utils.doWorkOnLifecycleScope
import ghazimoradi.soheil.recipeapp.viewmodels.RegisterViewModel

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentSplashBinding
        get() = FragmentSplashBinding::inflate

    private val viewModel: RegisterViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            bgImg.load(bg_splash)

            versionTxt.text = "${getString(version)} : $VERSION_NAME"

            doWorkOnLifecycleScope {
                3500.delay()
                viewModel.readData.asLiveData().observe(viewLifecycleOwner) {
                    findNavController().popBackStack(splashFragment, true)
                    findNavController().navigate(
                        if (it.username.isNotEmpty())
                            actionToRecipe
                        else actionToRegister
                    )
                }
            }
        }
    }
}