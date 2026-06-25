package ghazimoradi.soheil.recipeapp.data.repositories

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import ghazimoradi.soheil.recipeapp.data.models.register.BodyRegister
import ghazimoradi.soheil.recipeapp.data.models.register.RegisterStoredModel
import ghazimoradi.soheil.recipeapp.data.source.RemoteDataSource
import ghazimoradi.soheil.recipeapp.utils.REGISTER_HASH
import ghazimoradi.soheil.recipeapp.utils.REGISTER_USERNAME
import ghazimoradi.soheil.recipeapp.utils.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class RegisterRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val remote: RemoteDataSource
) {
    //Store user info
    private object StoredKeys {
        val username = stringPreferencesKey(REGISTER_USERNAME)
        val hash = stringPreferencesKey(REGISTER_HASH)
    }

    suspend fun saveRegisterData(username: String, hash: String) {
        context.dataStore.edit {
            it[StoredKeys.username] = username
            it[StoredKeys.hash] = hash
        }
    }

    val readRegisterData: Flow<RegisterStoredModel> = context.dataStore.data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val username = it[StoredKeys.username] ?: ""
            val hash = it[StoredKeys.hash] ?: ""
            RegisterStoredModel(username, hash)
        }

    //API
    suspend fun postRegister(apiKey: String, body: BodyRegister) = remote.postRegister(apiKey, body)
}