package ghazimoradi.soheil.recipeapp.ui.fragments.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.R.string.hello
import ghazimoradi.soheil.recipeapp.databinding.FragmentRecipeBinding
import ghazimoradi.soheil.recipeapp.utils.base.BaseFragment
import ghazimoradi.soheil.recipeapp.utils.doWorkOnLifecycleScope
import ghazimoradi.soheil.recipeapp.viewmodels.RegisterViewModel

@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class RecipeFragment : BaseFragment<FragmentRecipeBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentRecipeBinding
        get() = FragmentRecipeBinding::inflate

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showUsername()
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
}