package host.capitalquiz.arondit.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnBoardingViewModel: ViewModel() {

    private val _currentPage = MutableLiveData(0)
    val currentPage:LiveData<Int> = _currentPage

    fun updateCurrent(currentItem: Int) {
        if (_currentPage.value != currentItem) {
            _currentPage.value = currentItem
        }
    }
}