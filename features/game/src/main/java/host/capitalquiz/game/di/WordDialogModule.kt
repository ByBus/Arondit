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
interface WordDialogModule {

    @Binds
    fun bindWordInteractor(impl: WordInteractor.Base): WordInteractor

    @Binds
    fun bindWordRepository(impl: BaseWordRepository): WordRepository

    @Binds
    fun bindWordToWordDataMapperWithId(impl: WordToWordDataMapper): WordMapperWithParameter<Long, WordData>

    @Binds
    fun bindWordToUiMapper(impl: WordUiMapper.Base): WordUiMapper<Word>

    @Binds
    fun bindWordCacheDataSource(impl: WordDataDataSource.Base): WordDataDataSource

    @Binds
    fun bindWordBonusUpdater(impl: WordToWordMapper.BonusUpdater): WordToWordMapper

}