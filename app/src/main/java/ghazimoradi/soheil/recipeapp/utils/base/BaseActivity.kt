package ghazimoradi.soheil.recipeapp.utils.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors.fromApplication
import ghazimoradi.soheil.recipeapp.R.id.navHost
import ghazimoradi.soheil.recipeapp.di.ViewPumpEntryPoint
import io.github.inflationx.viewpump.ViewPumpContextWrapper.Companion.wrap

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    protected lateinit var navController: NavController

    override fun attachBaseContext(newBase: Context) {
        val viewPump = fromApplication(
            newBase.applicationContext,
            ViewPumpEntryPoint::class.java
        ).viewPump()

        super.attachBaseContext(wrap(newBase, viewPump))
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val navHostFragment = supportFragmentManager.findFragmentById(navHost) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }
}