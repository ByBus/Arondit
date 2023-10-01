package host.capitalquiz.arondit.core.datastore

import kotlinx.coroutines.flow.Flow

interface SettingsReadDataSource {
    val showOnBoardingScreen: Flow<Boolean>
}