package host.capitalquiz.editgamerule.ui.editscreen

data class EditableGameRuleUi(val id: Long, val name: String, val readOnly: Boolean, val letters: List<RuleLetter>)