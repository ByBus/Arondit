package host.capitalquiz.gameslist.domain

import kotlinx.coroutines.flow.Flow

interface SettingsReadRepository {

    val showOnBoarding: Flow<Boolean>

}