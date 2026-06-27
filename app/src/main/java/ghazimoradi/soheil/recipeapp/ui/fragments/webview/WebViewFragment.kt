package ghazimoradi.soheil.recipeapp.ui.fragments.webview

import android.view.LayoutInflater
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.databinding.FragmentWebViewBinding
import ghazimoradi.soheil.recipeapp.utils.base.BaseFragment

@AndroidEntryPoint
class WebViewFragment : BaseFragment<FragmentWebViewBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentWebViewBinding
        get() = FragmentWebViewBinding::inflate

}