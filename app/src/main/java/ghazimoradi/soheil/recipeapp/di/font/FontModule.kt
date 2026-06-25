package ghazimoradi.soheil.recipeapp.di.font

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FontModule {

    @Provides
    @Singleton
    fun provideViewPump(): ViewPump {
        return ViewPump.builder()
            .addInterceptor(
                CalligraphyInterceptor(
                    CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/atlas_regular.ttf")
                        .build()
                )
            ).build()
    }
}