package host.capitalquiz.editgamerule.domain

import javax.inject.Inject

interface CharProvider {

    fun provide(rule: GameRule): Char

    class NextChar @Inject constructor(): CharProvider {
        override fun provide(rule: GameRule): Char {
            val chars = rule.points.keys.sorted()
            if (chars.isEmpty()) return ' '

            var currentChar = chars.first()
            var isNextStillLetter = true

            while (rule.hasLetter(currentChar) && isNextStillLetter) {
                isNextStillLetter = currentChar.isLetter()
                currentChar++
            }
            return if (isNextStillLetter) currentChar else ' '
        }
    }
}