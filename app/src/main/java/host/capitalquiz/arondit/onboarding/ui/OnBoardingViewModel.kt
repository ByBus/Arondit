package host.capitalquiz.arondit.onboarding.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val settings: SettingsRepository,
) : ViewModel() {

    private val _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int> = _currentPage.distinctUntilChanged()

    val showOnBoarding = settings.showOnBoarding.asLiveData()

    fun updateCurrent(currentItem: Int) {
        _currentPage.value = currentItem
    }

    fun closeOnBoarding() {
        viewModelScope.launch {
            settings.disableOnBoarding()
        }
    }
}


