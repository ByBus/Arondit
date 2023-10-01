package host.capitalquiz.arondit.gameslist.domain

import kotlinx.coroutines.flow.Flow

interface SettingsReadRepository {

    val showOnBoarding: Flow<Boolean>

}