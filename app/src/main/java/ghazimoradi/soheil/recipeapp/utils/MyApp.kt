package ghazimoradi.soheil.recipeapp.utils

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    companion object {
        lateinit var myApplicationContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        myApplicationContext = applicationContext
    }
}