package host.capitalquiz.onboarding.ui

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val showOnBoarding: Flow<Boolean>

    suspend fun clear()
    suspend fun disableOnBoarding()
}