package ghazimoradi.soheil.recipeapp.utils

import android.content.Context
import android.content.res.ColorStateList
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getString
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.CachePolicy.ENABLED
import coil3.request.crossfade
import coil3.request.error
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar.make
import ghazimoradi.soheil.recipeapp.R.drawable.ic_placeholder
import ghazimoradi.soheil.recipeapp.utils.MyApp.Companion.myApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

val Context.registerUserInfoDataStore: DataStore<Preferences> by preferencesDataStore(
    REGISTER_USER_INFO
)

val Context.menuDataStore: DataStore<Preferences> by preferencesDataStore(MENU_DATASTORE)

fun View.showSnackBar(message: String, duration: Int = LENGTH_SHORT) =
    make(this, message, duration).show()

fun TextView.setDynamicallyColor(@ColorRes color: Int) {
    //Start - Left = 0 || Top = 1 || End - Right = 2 || Bottom = 3
    this.compoundDrawables[1].setTint(getColorResource(color))
    this.setTextColor(getColorResource(color))
}

fun RecyclerView.setupRecyclerview(
    myLayoutManager: RecyclerView.LayoutManager,
    myAdapter: RecyclerView.Adapter<*>,
) {
    this.apply {
        layoutManager = myLayoutManager
        setHasFixedSize(true)
        adapter = myAdapter
    }
}

fun Int.minToHour(): String {
    val time: String
    val hours: Int = this / 60
    val minutes: Int = this % 60
    time = if (hours > 0) "${hours}h:${minutes}min" else "${minutes}min"
    return time
}

fun <T> LiveData<T>.onceObserve(owner: LifecycleOwner, observe: Observer<T>) {
    observe(
        owner,
        object : Observer<T> {
            override fun onChanged(value: T) {
                removeObserver(this)
                observe.onChanged(value)
            }
        }
    )
}

fun View.isVisible(isShownLoading: Boolean, container: View) {
    if (isShownLoading) {
        this.isVisible = true
        container.isVisible = false
    } else {
        this.isVisible = false
        container.isVisible = true
    }
}

fun ImageView.setTint(@ColorRes color: Int) {
    imageTintList = ColorStateList.valueOf(getColorResource(color))
}

fun getColorResource(@ColorRes color: Int) = getColor(myApplicationContext, color)

fun getStringResource(@StringRes string: Int) = getString(myApplicationContext, string)

fun validateEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun Fragment.doWorkOnLifecycleScope(
    work: suspend () -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.CREATED) {
            work()
        }
    }
}

suspend fun Int.delay() = delay(this.milliseconds)

suspend fun Long.delay() = delay(this.milliseconds)

fun ImageView.loadImage(url: String) {
    load(url) {
        crossfade(true)
        crossfade(800)
        memoryCachePolicy(ENABLED)
        error(ic_placeholder)
    }
}