package host.capitalquiz.editgamerule.ui.dialog.mappers

import host.capitalquiz.editgamerule.domain.LetterAddResultMapperWithParameter
import host.capitalquiz.editgamerule.domain.LetterResult
import javax.inject.Inject
import host.capitalquiz.editgamerule.ui.dialog.AddLetterUiState as UiState

class CreateLetterAddResultToUiMapper @Inject constructor() :
    LetterAddResultMapperWithParameter<Boolean, UiState> {
    private var tryCloseAfterResult = false

    override fun map(result: LetterResult, param: Boolean): UiState {
        tryCloseAfterResult = param
        return result.map(this)
    }

    override fun mapSuccess(letter: Char, points: Int): UiState {
        return if (tryCloseAfterResult)
            UiState.Close
        else
            UiState.CreateNextLetter(letter, points)
    }

    override fun mapError(letter: Char, points: Int, oldPoints: Int): UiState = UiState.CreateSameLetter(letter, oldPoints)
}