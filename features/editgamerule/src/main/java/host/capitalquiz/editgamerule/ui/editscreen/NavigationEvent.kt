package host.capitalquiz.editgamerule.ui.editscreen

class NavigationEvent(val ruleId: Long, val letter: Char?, val points: Int) {
    fun consume(consumer: (Long, Char?, Int) -> Unit) = consumer.invoke(ruleId, letter, points)
}