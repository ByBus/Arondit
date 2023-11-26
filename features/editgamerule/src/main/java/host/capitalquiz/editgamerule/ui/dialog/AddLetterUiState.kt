package host.capitalquiz.editgamerule.ui.dialog

import androidx.core.view.isVisible
import androidx.transition.TransitionManager
import host.capitalquiz.core.ui.Dismissible
import host.capitalquiz.editgamerule.R
import host.capitalquiz.editgamerule.databinding.FragmentAddLetterDialogBinding
import host.capitalquiz.editgamerule.databinding.FragmentAddLetterDialogBinding as AddLetterBinding

sealed interface AddLetterUiState {

    fun update(binding: AddLetterBinding, dialog: Dismissible)

    abstract class BaseUiState : AddLetterUiState {
        override fun update(binding: AddLetterBinding, dialog: Dismissible) {
            TransitionManager.beginDelayedTransition(binding.content)
        }
        protected fun AddLetterBinding.setValues(letter: Char, points: Int){
            letterInput.editText?.setText(letter.toString())
            pointsInput.editText?.setText(points.toString())
        }

        protected fun mode(creationMode: Boolean, binding: AddLetterBinding) {
            val editMode = creationMode.not()
            binding.addLetterButton.isVisible = creationMode
            binding.selectedLetter.isVisible = editMode
            binding.removeLetterButton.isVisible = editMode
            binding.dialogHeader.setText( if (editMode) R.string.editing_of_letter else R.string.new_letter)
        }
    }

    abstract class BaseCreateUiState : BaseUiState() {
        override fun update(binding: AddLetterBinding, dialog: Dismissible) {
            super.update(binding, dialog)
            mode(true, binding)
        }
    }

    abstract class BaseEditUiState: BaseUiState() {
        override fun update(binding: FragmentAddLetterDialogBinding, dialog: Dismissible) {
            super.update(binding, dialog)
            mode(false, binding)
        }
    }

    object InitCreationMode : BaseCreateUiState() {
        override fun update(binding: AddLetterBinding, dialog: Dismissible) =
            binding.setValues(' ', -1)
    }

    class InitEditMode(private val letter: Char, private val points: Int): BaseEditUiState() {
        override fun update(binding: FragmentAddLetterDialogBinding, dialog: Dismissible) {
            super.update(binding, dialog)
            binding.selectedLetter.updateLetter(letter, points)
            binding.setValues(letter, points)
        }
    }

    object Close : BaseCreateUiState() {
        override fun update(binding: AddLetterBinding, dialog: Dismissible) = dialog.dismiss()
    }

    data class EditSameLetter(val letter: Char, val points: Int): BaseEditUiState() {
        override fun update(binding: FragmentAddLetterDialogBinding, dialog: Dismissible) {
            super.update(binding, dialog)
            with(binding) {
               errorMessage.isVisible = true
               errorMessage.setText(R.string.edit_letter_already_exist_hint)
               setValues(letter, points)
            }
        }
    }

    data class CreateNextLetter(val letter: Char, val points: Int) : BaseCreateUiState() {
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

    data class CreateSameLetter(val letter: Char, val points: Int) : BaseCreateUiState() {
        override fun update(binding: AddLetterBinding, dialog: Dismissible) {
            super.update(binding, dialog)
            with(binding) {
                okButton.isVisible = false
                addLetterButton.isVisible = true
                replaceButton.isVisible = true
                errorMessage.isVisible = true
                errorMessage.setText(R.string.add_letter_already_exist_hint)
                setValues(letter, points)
            }
        }
    }
}