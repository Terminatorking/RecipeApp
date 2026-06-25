package ghazimoradi.soheil.recipeapp.di.network

import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ghazimoradi.soheil.recipeapp.data.models.register.BodyRegister
import javax.inject.Singleton
import ghazimoradi.soheil.recipeapp.utils.network.CheckInternetConnection

@Module
@InstallIn(SingletonComponent::class)
object CheckConnection {

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideCheckInternet(
        connectivityManager: ConnectivityManager
    ): CheckInternetConnection = CheckInternetConnection(connectivityManager)

    @Provides
    fun bodyRegister() = BodyRegister()
}