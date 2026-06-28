package ghazimoradi.soheil.recipeapp.ui.fragments.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.databinding.FragmentWebViewBinding
import ghazimoradi.soheil.recipeapp.utils.base.BaseFragment

@SuppressLint("SetJavaScriptEnabled")
@AndroidEntryPoint
class WebViewFragment : BaseFragment<FragmentWebViewBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentWebViewBinding
        get() = FragmentWebViewBinding::inflate

    private val args: WebViewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            backImg.setOnClickListener {
                findNavController().popBackStack()
            }

            webView.apply {
                settings.apply {
                    javaScriptEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    builtInZoomControls = false
                    domStorageEnabled = true
                    databaseEnabled = true
                }

                webViewClient = WebViewClient()
                isVerticalScrollBarEnabled = true
                isHorizontalScrollBarEnabled = true

                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        if (newProgress < 100 && webViewLoading.visibility == View.GONE) {
                            webViewLoading.isVisible = true
                        }
                        webViewLoading.progress = newProgress
                        if (newProgress == 100) {
                            webViewLoading.isVisible = false
                        }
                    }
                }
                loadUrl(args.url)
            }
        }
    }
}