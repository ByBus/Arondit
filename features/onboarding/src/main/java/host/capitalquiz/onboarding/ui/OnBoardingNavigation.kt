package host.capitalquiz.onboarding.ui

interface OnBoardingNavigation {
    fun navigateToGameFragment(gameId: Long)
    fun navigateBack()
}