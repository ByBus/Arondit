package host.capitalquiz.arondit.game.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import host.capitalquiz.arondit.core.db.WordData
import host.capitalquiz.arondit.game.data.BaseWordRepository
import host.capitalquiz.arondit.game.data.WordToWordDataMapper
import host.capitalquiz.arondit.game.domain.Word
import host.capitalquiz.arondit.game.domain.WordInteractor
import host.capitalquiz.arondit.game.domain.WordMapperWithId
import host.capitalquiz.arondit.game.domain.WordRepository
import host.capitalquiz.arondit.game.ui.WordUiMapper

@Module
@InstallIn(ViewModelComponent::class)
abstract class WordDialogModule {

    @Binds
    abstract fun bindWordInteractor(impl: WordInteractor.Base) : WordInteractor

    @Binds
    abstract fun bindWordRepository(impl: BaseWordRepository): WordRepository

    @Binds
    abstract fun bindWordToWordDataMapperWithId(impl: WordToWordDataMapper): WordMapperWithId<Long, WordData>

    @Binds
    abstract fun bindWordToUiMapper(impl: WordUiMapper.Base): WordUiMapper<Word>

}