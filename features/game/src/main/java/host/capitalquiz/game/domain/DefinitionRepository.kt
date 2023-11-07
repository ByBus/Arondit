package host.capitalquiz.game.domain

interface DefinitionRepository {
    suspend fun findDefinition(word: String): WordDefinition
}