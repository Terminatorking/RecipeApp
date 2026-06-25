package ghazimoradi.soheil.recipeapp.ui.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil3.load
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.R.drawable.register_logo
import ghazimoradi.soheil.recipeapp.R.id.actionToRecipe
import ghazimoradi.soheil.recipeapp.R.id.registerFragment
import ghazimoradi.soheil.recipeapp.R.string.checkConnection
import ghazimoradi.soheil.recipeapp.R.string.emailNotValid
import ghazimoradi.soheil.recipeapp.data.models.register.BodyRegister
import ghazimoradi.soheil.recipeapp.databinding.FragmentRegisterBinding
import ghazimoradi.soheil.recipeapp.utils.MY_API_KEY
import ghazimoradi.soheil.recipeapp.utils.base.BaseFragment
import ghazimoradi.soheil.recipeapp.utils.doWorkOnLifecycleScope
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest.Error
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest.Loading
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest.Success
import ghazimoradi.soheil.recipeapp.utils.showSnackBar
import ghazimoradi.soheil.recipeapp.utils.validateEmail
import ghazimoradi.soheil.recipeapp.viewmodels.RegisterViewModel
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentRegisterBinding
        get() = FragmentRegisterBinding::inflate

    private var email = ""
    private val viewModel: RegisterViewModel by viewModels()

    @Inject
    lateinit var body: BodyRegister

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            coverImg.load(register_logo)

            emailEdt.addTextChangedListener {
                val value = it.toString()
                if (validateEmail(value)) {
                    email = value
                    emailTxtLay.error = ""
                } else {
                    emailTxtLay.error = getString(emailNotValid)
                }
            }

            submitBtn.setOnClickListener {
                val firstname = nameEdt.text.toString()
                val lastName = lastNameEdt.text.toString()
                val username = usernameEdt.text.toString()

                body.email = email
                body.firstName = firstname
                body.lastName = lastName
                body.username = username

                doWorkOnLifecycleScope {
                    if (isNetworkAvailable) {
                        viewModel.callRegisterApi(MY_API_KEY, body)
                    } else {
                        root.showSnackBar(getString(checkConnection))
                    }
                }
            }

            loadRegisterData()
        }
    }


    private fun loadRegisterData() {
        viewModel.registerData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Loading -> {}

                is Success -> {
                    response.data?.let { data ->
                        viewModel.saveData(data.username.toString(), data.hash.toString())
                        findNavController().popBackStack(registerFragment, true)
                        findNavController().navigate(actionToRecipe)
                    }
                }

                is Error -> {
                    binding.root.showSnackBar(response.message.toString())
                }
            }
        }
    }
}