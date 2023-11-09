package host.capitalquiz.gameslist.data

import host.capitalquiz.core.datastore.SettingsLocalDataSource
import host.capitalquiz.gameslist.domain.SettingsReadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BaseSettingsRepository @Inject constructor(
    storage: SettingsLocalDataSource
): SettingsReadRepository {

    override val showOnBoarding: Flow<Boolean> = storage.showOnBoardingScreen

}