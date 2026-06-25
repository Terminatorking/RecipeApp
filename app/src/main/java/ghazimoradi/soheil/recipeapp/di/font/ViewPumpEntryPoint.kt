package ghazimoradi.soheil.recipeapp.di.font

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.inflationx.viewpump.ViewPump

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ViewPumpEntryPoint {
    fun viewPump(): ViewPump
}