package host.capitalquiz.core.datastore

import kotlinx.coroutines.flow.Flow

interface SettingsReadDataSource {
    val showOnBoardingScreen: Flow<Boolean>
}