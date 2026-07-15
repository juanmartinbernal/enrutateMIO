package com.enrutatemio.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Preferencias de usuario (reemplaza el uso disperso de `SharedPreferences`/`GlobalData`
 * de la app legacy por un único DataStore tipado).
 */
class UserPreferencesDataStore(private val dataStore: DataStore<Preferences>) {

    val tutorialShown: Flow<Boolean> =
        dataStore.data.map { it[Keys.TUTORIAL_SHOWN] ?: false }

    val newsPollingEnabled: Flow<Boolean> =
        dataStore.data.map { it[Keys.NEWS_POLLING_ENABLED] ?: true }

    val notificationsEnabled: Flow<Boolean> =
        dataStore.data.map { it[Keys.NOTIFICATIONS_ENABLED] ?: true }

    val lastFeederZone: Flow<String?> =
        dataStore.data.map { it[Keys.LAST_FEEDER_ZONE] }

    val hybridMapEnabled: Flow<Boolean> =
        dataStore.data.map { it[Keys.HYBRID_MAP_ENABLED] ?: false }

    suspend fun setTutorialShown(shown: Boolean) {
        dataStore.edit { it[Keys.TUTORIAL_SHOWN] = shown }
    }

    suspend fun setNewsPollingEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.NEWS_POLLING_ENABLED] = enabled }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.NOTIFICATIONS_ENABLED] = enabled }
    }

    suspend fun setLastFeederZone(zone: String?) {
        dataStore.edit {
            if (zone == null) it.remove(Keys.LAST_FEEDER_ZONE) else it[Keys.LAST_FEEDER_ZONE] = zone
        }
    }

    suspend fun setHybridMapEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.HYBRID_MAP_ENABLED] = enabled }
    }

    private object Keys {
        val TUTORIAL_SHOWN = booleanPreferencesKey("tutorial_shown")
        val NEWS_POLLING_ENABLED = booleanPreferencesKey("news_polling_enabled")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val LAST_FEEDER_ZONE = stringPreferencesKey("last_feeder_zone")
        val HYBRID_MAP_ENABLED = booleanPreferencesKey("hybrid_map_enabled")
    }
}
