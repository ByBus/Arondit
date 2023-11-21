package host.capitalquiz.editgamerule.ui.dialog

import androidx.core.view.isVisible
import androidx.transition.TransitionManager
import host.capitalquiz.editgamerule.databinding.FragmentAddLetterDialogBinding
import host.capitalquiz.editgamerule.databinding.FragmentAddLetterDialogBinding as AddLetterBinding

sealed interface AddLetterUiState {

    fun update(binding: AddLetterBinding, dialog: Dismissible)

    abstract class BaseUiState : AddLetterUiState {
        override fun update(binding: FragmentAddLetterDialogBinding, dialog: Dismissible) =
            TransitionManager.beginDelayedTransition(binding.content)

        protected fun AddLetterBinding.setValues(letter: Char, points: Int){
            letterInput.editText?.setText(letter.toString())
            pointsInput.editText?.setText(points.toString())
        }
    }

    object Init : BaseUiState() {
        override fun update(binding: FragmentAddLetterDialogBinding, dialog: Dismissible) =
            binding.setValues(' ', -1)
    }

    object Close : BaseUiState() {
        override fun update(binding: AddLetterBinding, dialog: Dismissible) = dialog.dismiss()
    }

    data class NextLetter(val letter: Char, val points: Int) : BaseUiState() {
        override fun update(binding: AddLetterBinding, dialog: Dismissible) {
            super.update(binding, dialog)
            with(binding) {
                okButton.isVisible = true
                addLetterButton.isVisible = true
                replaceButton.isVisible = false
                errorMessage.isVisible = false
                setValues(letter, points)
            }
        }
    }

    data class SameLetter(val letter: Char, val points: Int) : BaseUiState() {
        override fun update(binding: AddLetterBinding, dialog: Dismissible) {
            super.update(binding, dialog)
            with(binding) {
                okButton.isVisible = false
                addLetterButton.isVisible = true
                replaceButton.isVisible = true
                errorMessage.isVisible = true
                setValues(letter, points)
            }
        }
    }
}