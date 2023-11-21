package host.capitalquiz.editgamerule.ui.dialog

import host.capitalquiz.editgamerule.domain.LetterAddResult
import host.capitalquiz.editgamerule.domain.LetterAddResultMapperWithParameter
import javax.inject.Inject
import host.capitalquiz.editgamerule.ui.dialog.AddLetterUiState as UiState

class LetterAddResultToUiMapper @Inject constructor() :
    LetterAddResultMapperWithParameter<Boolean, UiState> {
    private var tryCloseAfterResult = false

    override fun map(result: LetterAddResult, param: Boolean): UiState {
        tryCloseAfterResult = param
        return result.map(this)
    }

    override fun mapSuccess(letter: Char, points: Int): UiState {
        return if (tryCloseAfterResult)
            UiState.Close
        else
            UiState.NextLetter(letter, points)
    }

    override fun mapError(letter: Char, points: Int): UiState = UiState.SameLetter(letter, points)
}