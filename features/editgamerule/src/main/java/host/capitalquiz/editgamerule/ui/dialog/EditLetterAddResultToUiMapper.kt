package host.capitalquiz.editgamerule.ui.dialog

import host.capitalquiz.editgamerule.domain.LetterAddResultMapper
import javax.inject.Inject

class EditLetterAddResultToUiMapper @Inject constructor(): LetterAddResultMapper<AddLetterUiState> {
    override fun mapSuccess(letter: Char, points: Int): AddLetterUiState {
        return AddLetterUiState.Close
    }

    override fun mapError(letter: Char, points: Int, oldPoints: Int): AddLetterUiState {
        return AddLetterUiState.EditSameLetter(letter, points)
    }
}