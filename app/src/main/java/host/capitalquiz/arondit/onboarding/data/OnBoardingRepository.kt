package host.capitalquiz.arondit.onboarding.data

import host.capitalquiz.arondit.core.datastore.SettingsLocalDataSource
import host.capitalquiz.arondit.onboarding.ui.SettingsRepository
import javax.inject.Inject

class OnBoardingRepository @Inject constructor(
    private val storage: SettingsLocalDataSource,
) : SettingsRepository {

    override val showOnBoarding = storage.showOnBoardingScreen
    override suspend fun clear() {
        storage.clear()
    }

    override suspend fun disableOnBoarding() {
        storage.enableOnBoardingScreen(false)
    }
}