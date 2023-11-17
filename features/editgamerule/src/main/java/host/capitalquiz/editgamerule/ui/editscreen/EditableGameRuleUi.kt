package host.capitalquiz.editgamerule.ui.editscreen

data class EditableGameRuleUi(
    val id: Long,
    val name: String,
    val readOnly: Boolean,
    val letters: List<RuleLetter>,
) {
    fun <R> map(mapper: EditableGameRuleUiMapper<R>): R {
        return mapper(id, name, readOnly, letters)
    }

    companion object {
        fun empty() = EditableGameRuleUi(0L, "", false, emptyList())
    }
}
