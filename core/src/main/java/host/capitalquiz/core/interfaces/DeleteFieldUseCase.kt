package host.capitalquiz.core.interfaces

interface DeleteFieldUseCase {
    suspend operator fun invoke(fieldId: Long)
}