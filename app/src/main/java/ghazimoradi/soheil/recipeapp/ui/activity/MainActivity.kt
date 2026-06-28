package ghazimoradi.soheil.recipeapp.ui.activity

import android.content.Context
import android.os.Bundle
import android.graphics.Color.parseColor
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors.fromApplication
import ghazimoradi.soheil.recipeapp.R.id.actionToMenu
import ghazimoradi.soheil.recipeapp.R.id.detailFragment
import ghazimoradi.soheil.recipeapp.R.id.navHost
import ghazimoradi.soheil.recipeapp.R.id.registerFragment
import ghazimoradi.soheil.recipeapp.R.id.splashFragment
import ghazimoradi.soheil.recipeapp.R.id.stepsFragment
import ghazimoradi.soheil.recipeapp.R.id.webViewFragment
import ghazimoradi.soheil.recipeapp.databinding.ActivityMainBinding
import ghazimoradi.soheil.recipeapp.di.font.ViewPumpEntryPoint
import io.github.inflationx.viewpump.ViewPumpContextWrapper.Companion.wrap

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!

    override fun attachBaseContext(newBase: Context) {
        val viewPump = fromApplication(
            newBase.applicationContext,
            ViewPumpEntryPoint::class.java
        ).viewPump()

        super.attachBaseContext(wrap(newBase, viewPump))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            v.setBackgroundColor(parseColor("#1D1B33"))
            insets
        }

        setOnApplyWindowInsetsListener(binding.mainBottomAppbar) { v, insets ->
            v.setPadding(0, 0, 0, 10)
            insets
        }

        setupNav(navHost)
        binding.apply {
            mainBottomNav.itemActiveIndicatorColor = null
            mainBottomNav.setupWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    splashFragment -> visibilityBottomMenu(false)
                    registerFragment -> visibilityBottomMenu(false)
                    detailFragment -> visibilityBottomMenu(false)
                    webViewFragment -> visibilityBottomMenu(false)
                    stepsFragment -> visibilityBottomMenu(false)
                    else -> visibilityBottomMenu(true)
                }
            }

            mainFabMenu.setOnClickListener {
               navController.navigate(actionToMenu)
            }

        }
    }

    private fun visibilityBottomMenu(isVisibility: Boolean) {
        binding.apply {
            if (isVisibility) {
                mainBottomAppbar.isVisible = true
                mainFabMenu.isVisible = true
            } else {
                mainBottomAppbar.isVisible = false
                mainFabMenu.isVisible = false
            }
        }
    }

    fun setupNav(@IdRes id: Int) {
        val navHostFragment = supportFragmentManager.findFragmentById(id) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}