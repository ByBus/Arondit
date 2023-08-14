package host.capitalquiz.arondit.game.domain

interface DefinitionRepository {
    suspend fun findDefinition(word: String): WordDefinition
}