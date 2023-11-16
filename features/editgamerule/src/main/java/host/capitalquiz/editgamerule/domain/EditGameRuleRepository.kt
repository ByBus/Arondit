package host.capitalquiz.editgamerule.domain

import kotlinx.coroutines.flow.Flow

interface EditGameRuleRepository {
    fun findRuleById(id: Long): Flow<GameRule>
}