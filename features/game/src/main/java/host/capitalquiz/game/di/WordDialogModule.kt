package host.capitalquiz.game.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import host.capitalquiz.core.db.WordData
import host.capitalquiz.game.data.BaseWordRepository
import host.capitalquiz.game.data.WordDataDataSource
import host.capitalquiz.game.data.WordToWordDataMapper
import host.capitalquiz.game.domain.Word
import host.capitalquiz.game.domain.WordInteractor
import host.capitalquiz.game.domain.WordMapperWithParameter
import host.capitalquiz.game.domain.WordRepository
import host.capitalquiz.game.domain.WordToWordMapper
import host.capitalquiz.game.ui.WordUiMapper

@Module
@InstallIn(ViewModelComponent::class)
abstract class WordDialogModule {

    @Binds
    abstract fun bindWordInteractor(impl: WordInteractor.Base) : WordInteractor

    @Binds
    abstract fun bindWordRepository(impl: BaseWordRepository): WordRepository

    @Binds
    abstract fun bindWordToWordDataMapperWithId(impl: WordToWordDataMapper): WordMapperWithParameter<Long, Word, WordData>

    @Binds
    abstract fun bindWordToUiMapper(impl: WordUiMapper.Base): WordUiMapper<Word>

    @Binds
    abstract fun bindWordCacheDataSource(impl: WordDataDataSource.Base): WordDataDataSource

    @Binds
    abstract fun bindWordBonusUpdater(impl: WordToWordMapper.BonusUpdater): WordToWordMapper

}