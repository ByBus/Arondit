package host.capitalquiz.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SettingsLocalDataSource: SettingsReadDataSource {
    suspend fun enableOnBoardingScreen(enable: Boolean)
    suspend fun clear()

    class BaseDataStore(
        private val dataStore: DataStore<Preferences>,
    ) : SettingsLocalDataSource {

        override val showOnBoardingScreen: Flow<Boolean> = dataStore.data
            .map { settings ->
                settings[SHOW_ON_BOARDING] ?: true
            }

        override suspend fun enableOnBoardingScreen(enable: Boolean) {
            dataStore.edit { settings ->
                settings[SHOW_ON_BOARDING] = enable
            }
        }

        override suspend fun clear() {
            dataStore.edit {
                it.clear()
            }
        }

        private companion object SettingKey {
            private val SHOW_ON_BOARDING = booleanPreferencesKey("show_on_boarding")
        }
    }

}