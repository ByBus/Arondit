package host.capitalquiz.game.ui.dialog

import host.capitalquiz.core.ui.Dismissible

class AddPlayerEvent(private val added: Boolean) {
    fun consume(dialog: Dismissible) {
        if (added) dialog.dismiss()
    }
}