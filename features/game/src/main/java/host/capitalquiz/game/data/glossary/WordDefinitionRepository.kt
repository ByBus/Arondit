package host.capitalquiz.game.data.glossary

import host.capitalquiz.game.di.CompositeGlossary
import host.capitalquiz.game.di.IoDispatcher
import host.capitalquiz.game.domain.DefinitionRepository
import host.capitalquiz.game.domain.WordDefinition
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class WordDefinitionRepository @Inject constructor(
    @CompositeGlossary
    private val glossary: Glossary,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
) : DefinitionRepository {
    override suspend fun findDefinition(word: String): WordDefinition {
        return withContext(dispatcher) {
            glossary.requestDefinition(word)
        }
    }
}