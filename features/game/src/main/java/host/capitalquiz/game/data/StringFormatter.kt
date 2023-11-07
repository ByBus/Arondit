package host.capitalquiz.game.data

import javax.inject.Inject

interface StringFormatter {
    fun format(string: String): String

    class UnwantedCharToAsteriskReplacer @Inject constructor(
        dictionary: Map<Char, Int>
    ): StringFormatter {
        private val allowedChars = dictionary.keys
            .joinToString("", prefix = "[^", postfix = "]") { it.toString() }
            .toRegex()

        override fun format(string: String): String {
            return string.uppercase().replace(allowedChars, "*")
        }
    }
}