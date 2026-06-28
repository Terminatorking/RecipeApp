package ghazimoradi.soheil.recipeapp.ui.fragments.lucky

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.text.HtmlCompat.fromHtml
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable.createFromAttributes
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.R.color.caribbean_green
import ghazimoradi.soheil.recipeapp.R.color.chineseYellow
import ghazimoradi.soheil.recipeapp.R.color.darkGray
import ghazimoradi.soheil.recipeapp.R.color.tart_orange
import ghazimoradi.soheil.recipeapp.R.string.items
import ghazimoradi.soheil.recipeapp.R.style.DietsChip
import ghazimoradi.soheil.recipeapp.data.models.detail.ResponseDetail.AnalyzedInstruction.Step
import ghazimoradi.soheil.recipeapp.data.models.detail.ResponseDetail.ExtendedIngredient
import ghazimoradi.soheil.recipeapp.data.models.lucky.ResponseLucky.Recipe
import ghazimoradi.soheil.recipeapp.databinding.FragmentLuckyBinding
import ghazimoradi.soheil.recipeapp.ui.adapters.InstructionsAdapter
import ghazimoradi.soheil.recipeapp.ui.adapters.StepsAdapter
import ghazimoradi.soheil.recipeapp.ui.fragments.detail.DetailFragmentDirections.actionDetailToSteps
import ghazimoradi.soheil.recipeapp.ui.fragments.detail.DetailFragmentDirections.actionToWebView
import ghazimoradi.soheil.recipeapp.utils.NEW_IMAGE_SIZE
import ghazimoradi.soheil.recipeapp.utils.OLD_IMAGE_SIZE
import ghazimoradi.soheil.recipeapp.utils.STEPS_COUNT
import ghazimoradi.soheil.recipeapp.utils.base.BaseFragment
import ghazimoradi.soheil.recipeapp.utils.getColorResource
import ghazimoradi.soheil.recipeapp.utils.isVisible
import ghazimoradi.soheil.recipeapp.utils.loadImage
import ghazimoradi.soheil.recipeapp.utils.minToHour
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest.Error
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest.Loading
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest.Success
import ghazimoradi.soheil.recipeapp.utils.setDynamicallyColor
import ghazimoradi.soheil.recipeapp.utils.setupRecyclerview
import ghazimoradi.soheil.recipeapp.utils.showSnackBar
import ghazimoradi.soheil.recipeapp.viewmodels.LuckyViewModel
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class LuckyFragment : BaseFragment<FragmentLuckyBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentLuckyBinding
        get() = FragmentLuckyBinding::inflate

    private val viewModel: LuckyViewModel by viewModels()

    @Inject
    lateinit var instructionsAdapter: InstructionsAdapter

    @Inject
    lateinit var stepsAdapter: StepsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInternetLayout(isNetworkAvailable)
        if (isNetworkAvailable) viewModel.callLuckyApi()

        loadDetailDataFromApi()
    }

    private fun loadDetailDataFromApi() {
        binding.apply {
            viewModel.luckyData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Loading -> {
                        loading.isVisible(true, contentLay)
                    }

                    is Success -> {
                        loading.isVisible(false, contentLay)
                        response.data?.let { data ->
                            initViewsWithData(data.recipes!![0])
                        }
                    }

                    is Error -> {
                        loading.isVisible(false, contentLay)
                        binding.root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }

    private fun initViewsWithData(data: Recipe) {
        binding.apply {

            data.image?.let {
                val imageSplit = it.split("-")
                val imageSize = imageSplit[1].replace(OLD_IMAGE_SIZE, NEW_IMAGE_SIZE)
                coverImg.loadImage("${imageSplit[0]}-$imageSize")
            }

            data.sourceUrl?.let { source ->
                sourceImg.isVisible = true
                sourceImg.setOnClickListener {
                    val direction = actionToWebView(source)
                    findNavController().navigate(direction)
                }
            }

            data.readyInMinutes?.let {
                timeTxt.text = it.minToHour()
            }

            nameTxt.text = data.title

            data.summary?.let {
                val summary = fromHtml(it, FROM_HTML_MODE_COMPACT)
                descTxt.text = summary
            }

            if (data.cheap ?: false) cheapTxt.setDynamicallyColor(caribbean_green)
            if (data.veryPopular ?: false) popularTxt.setDynamicallyColor(tart_orange)
            if (data.vegan ?: false) veganTxt.setDynamicallyColor(caribbean_green)
            if (data.dairyFree ?: false) dairyTxt.setDynamicallyColor(caribbean_green)

            likeTxt.text = data.aggregateLikes.toString()

            priceTxt.text = "${data.pricePerServing} $"

            healthyTxt.text = data.healthScore.toString()

            when (data.healthScore) {
                in 90..100 -> healthyTxt.setDynamicallyColor(caribbean_green)
                in 60..89 -> healthyTxt.setDynamicallyColor(chineseYellow)
                in 0..59 -> healthyTxt.setDynamicallyColor(tart_orange)
            }

            val extendedIngredients = data.extendedIngredients ?: emptyList()

            instructionsCount.text = "${extendedIngredients.size} ${getString(items)}"

            val instructions =
                fromHtml(data.instructions ?: "No Instructions", FROM_HTML_MODE_COMPACT)
            instructionsDesc.text = instructions

            initInstructionsList(extendedIngredients.toMutableList())

            val analyzedInstructions = data.analyzedInstructions ?: emptyList()

            analyzedInstructions[0].steps?.let { steps ->
                initStepsList(steps.toMutableList())
            }

            stepsShowMore.setOnClickListener {
                findNavController().navigate(actionDetailToSteps(analyzedInstructions[0]))
            }

            setupChip(data.diets!!.toMutableList(), dietsChipGroup)
        }
    }

    private fun initInstructionsList(list: MutableList<ExtendedIngredient>) {
        if (list.isNotEmpty()) {
            instructionsAdapter.setData(list)
            binding.instructionsList.setupRecyclerview(
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                ),
                instructionsAdapter
            )
        }
    }

    private fun initStepsList(list: MutableList<Step>) {
        if (list.isNotEmpty()) {
            STEPS_COUNT = if (list.size < 3) {
                list.size
            } else {
                3
            }

            stepsAdapter.setData(list)

            binding.apply {
                stepsList.setupRecyclerview(
                    LinearLayoutManager(requireContext()),
                    stepsAdapter
                )

                if (list.size > 3) {
                    stepsShowMore.isVisible = true
                }
            }
        }
    }

    private fun setupChip(list: MutableList<String>, view: ChipGroup) {
        list.forEach {
            val chip = Chip(requireContext())

            val drawable = createFromAttributes(
                requireContext(),
                null,
                0,
                DietsChip
            )

            chip.setChipDrawable(drawable)
            chip.setTextColor(getColorResource(darkGray))
            chip.text = it
            view.addView(chip)
        }
    }

    private fun initInternetLayout(isConnected: Boolean) {
        binding.apply {
            internetLay.isVisible = isConnected.not()
            contentLay.isVisible = isConnected
        }
    }
}