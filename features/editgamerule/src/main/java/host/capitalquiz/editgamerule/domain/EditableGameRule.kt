package host.capitalquiz.editgamerule.domain

class EditableGameRule(val id: Long, val name: String, val lettersToPoints: Map<Char, Int>)