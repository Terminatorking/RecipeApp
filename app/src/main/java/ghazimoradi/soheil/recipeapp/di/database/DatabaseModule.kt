package ghazimoradi.soheil.recipeapp.di.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ghazimoradi.soheil.recipeapp.data.database.RecipeAppDatabase
import ghazimoradi.soheil.recipeapp.utils.DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        RecipeAppDatabase::class.java,
        DATABASE_NAME
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration(false)
        .build()

    @Provides
    @Singleton
    fun provideDao(database: RecipeAppDatabase) = database.dao()
}