package host.capitalquiz.arondit.gameslist.data

import host.capitalquiz.arondit.gameslist.domain.SettingsReadRepository
import host.capitalquiz.core.datastore.SettingsLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BaseSettingsRepository @Inject constructor(
    storage: SettingsLocalDataSource
): SettingsReadRepository {

    override val showOnBoarding: Flow<Boolean> = storage.showOnBoardingScreen

}