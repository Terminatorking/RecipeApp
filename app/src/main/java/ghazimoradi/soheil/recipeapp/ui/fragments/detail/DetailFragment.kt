package ghazimoradi.soheil.recipeapp.ui.fragments.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.text.HtmlCompat.fromHtml
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable.createFromAttributes
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.R.color.caribbean_green
import ghazimoradi.soheil.recipeapp.R.color.chineseYellow
import ghazimoradi.soheil.recipeapp.R.color.darkGray
import ghazimoradi.soheil.recipeapp.R.color.persianGreen
import ghazimoradi.soheil.recipeapp.R.color.tart_orange
import ghazimoradi.soheil.recipeapp.R.drawable.ic_heart_circle_minus
import ghazimoradi.soheil.recipeapp.R.drawable.ic_heart_circle_plus
import ghazimoradi.soheil.recipeapp.R.string.items
import ghazimoradi.soheil.recipeapp.R.style.DietsChip
import ghazimoradi.soheil.recipeapp.data.models.database.entities.FavoriteEntity
import ghazimoradi.soheil.recipeapp.data.models.detail.ResponseDetail
import ghazimoradi.soheil.recipeapp.data.models.detail.ResponseDetail.AnalyzedInstruction.Step
import ghazimoradi.soheil.recipeapp.data.models.detail.ResponseDetail.ExtendedIngredient
import ghazimoradi.soheil.recipeapp.data.models.detail.ResponseSimilar.ResponseSimilarItem
import ghazimoradi.soheil.recipeapp.databinding.FragmentDetailBinding
import ghazimoradi.soheil.recipeapp.ui.adapters.InstructionsAdapter
import ghazimoradi.soheil.recipeapp.ui.adapters.SimilarAdapter
import ghazimoradi.soheil.recipeapp.ui.adapters.StepsAdapter
import ghazimoradi.soheil.recipeapp.ui.fragments.detail.DetailFragmentDirections.actionToDetail
import ghazimoradi.soheil.recipeapp.ui.fragments.detail.DetailFragmentDirections.actionToWebView
import ghazimoradi.soheil.recipeapp.ui.fragments.detail.DetailFragmentDirections.actionDetailToSteps
import ghazimoradi.soheil.recipeapp.utils.MY_API_KEY
import ghazimoradi.soheil.recipeapp.utils.NEW_IMAGE_SIZE
import ghazimoradi.soheil.recipeapp.utils.OLD_IMAGE_SIZE
import ghazimoradi.soheil.recipeapp.utils.STEPS_COUNT
import ghazimoradi.soheil.recipeapp.utils.base.BaseFragment
import ghazimoradi.soheil.recipeapp.utils.delay
import ghazimoradi.soheil.recipeapp.utils.doWorkOnLifecycleScope
import ghazimoradi.soheil.recipeapp.utils.getColorResource
import ghazimoradi.soheil.recipeapp.utils.isVisible
import ghazimoradi.soheil.recipeapp.utils.loadImage
import ghazimoradi.soheil.recipeapp.utils.minToHour
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest.Error
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest.Loading
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest.Success
import ghazimoradi.soheil.recipeapp.utils.setDynamicallyColor
import ghazimoradi.soheil.recipeapp.utils.setTint
import ghazimoradi.soheil.recipeapp.utils.setupRecyclerview
import ghazimoradi.soheil.recipeapp.utils.showSnackBar
import ghazimoradi.soheil.recipeapp.viewmodels.DetailViewModel
import javax.inject.Inject

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentDetailBinding
        get() = FragmentDetailBinding::inflate

    private var recipeId = 0
    private var isExistsCache = false
    private var isExistsFavorite = false

    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    @Inject
    lateinit var instructionsAdapter: InstructionsAdapter

    @Inject
    lateinit var stepsAdapter: StepsAdapter

    @Inject
    lateinit var similarAdapter: SimilarAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.recipeID.let {
            recipeId = it
            if (it > 0) checkExistsDetailInCache(recipeId)
        }

        binding.backImg.setOnClickListener {
            findNavController().popBackStack()
        }

        doWorkOnLifecycleScope {
            200.delay()
            if (isExistsCache.not()) {
                initInternetLayout(isNetworkAvailable)
                if (isNetworkAvailable) {
                    loadDetailDataFromApi()
                    viewModel.callSimilarApi(recipeId, MY_API_KEY)
                }
            }
            if (isNetworkAvailable) {
                viewModel.callSimilarApi(recipeId, MY_API_KEY)
            }
        }

        loadSimilarData()
    }

    private fun loadDetailDataFromApi() {
        viewModel.callDetailApi(recipeId, MY_API_KEY)
        binding.apply {
            viewModel.detailData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Loading -> {
                        loading.isVisible(true, contentLay)
                    }

                    is Success -> {
                        loading.isVisible(false, contentLay)
                        response.data?.let { data ->
                            initViewsWithData(data)
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

    private fun checkExistsDetailInCache(id: Int) {
        viewModel.existsDetail(id)

        viewModel.existsDetailData.observe(viewLifecycleOwner) {
            isExistsCache = it
            if (it) {
                loadDetailDataFromDb()
                binding.contentLay.isVisible = true
            }
        }
    }

    private fun loadDetailDataFromDb() {
        viewModel.readDetailFromDb(recipeId).observe(viewLifecycleOwner) {
            initViewsWithData(it.result)
        }
    }

    private fun initViewsWithData(data: ResponseDetail) {
        binding.apply {

            viewModel.existsFavorite(data.id!!)
            checkExistsFavorite()

            favoriteImg.setOnClickListener {
                if (isExistsFavorite) deleteFavorite(data) else saveFavorite(data)
            }

            data.image?.let { image ->
                val imageSplit = image.split("-")
                val imageSize = imageSplit[1].replace(OLD_IMAGE_SIZE, NEW_IMAGE_SIZE)
                coverImg.loadImage("${imageSplit[0]}-$imageSize")
            }

            data.sourceUrl?.let { source ->
                sourceImg.isVisible = true
                sourceImg.setOnClickListener {
                    findNavController().navigate(actionToWebView(source))
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
                fromHtml(data.instructions ?: "No instructions!", FROM_HTML_MODE_COMPACT)

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
                    stepsAdapter,
                )

                if (list.size > 3) {
                    stepsShowMore.isVisible = true
                }
            }
        }
    }

    private fun initSimilarData(list: MutableList<ResponseSimilarItem>) {
        similarAdapter.setData(list)

        binding.similarList.setupRecyclerview(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            ),
            similarAdapter
        )

        similarAdapter.setOnItemClickListener {
            findNavController().navigate(actionToDetail(it))
        }
    }

    private fun loadSimilarData() {
        binding.apply {
            viewModel.similarData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Loading -> {
                        similarList.showShimmer()
                    }

                    is Success -> {
                        similarList.hideShimmer()
                        response.data?.let { data ->
                            initSimilarData(data)
                        }
                    }

                    is Error -> {
                        similarList.hideShimmer()
                        binding.root.showSnackBar(response.message.toString())
                    }
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

    private fun saveFavorite(data: ResponseDetail) {
        val entity = FavoriteEntity(data.id!!, data)
        viewModel.saveFavorite(entity)
        binding.favoriteImg.apply {
            setTint(tart_orange)
            setImageResource(ic_heart_circle_minus)
        }
    }

    private fun deleteFavorite(data: ResponseDetail) {
        val entity = FavoriteEntity(data.id!!, data)
        viewModel.deleteFavorite(entity)
        binding.favoriteImg.apply {
            setTint(persianGreen)
            setImageResource(ic_heart_circle_plus)
        }
    }

    private fun checkExistsFavorite() {
        viewModel.existsFavoriteData.observe(viewLifecycleOwner) {
            binding.apply {
                isExistsFavorite = it
                val color = if (it) tart_orange else persianGreen
                val image = if (it) ic_heart_circle_minus else ic_heart_circle_plus

                favoriteImg.apply {
                    setTint(color)
                    setImageResource(image)
                }
            }
        }
    }
}