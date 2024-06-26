package host.capitalquiz.arondit.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import host.capitalquiz.arondit.implementation.BaseDeleteFieldUseCase
import host.capitalquiz.core.interfaces.DeleteFieldUseCase

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {

    @Binds
    fun bindDeletePlayerOfFieldUseCase(impl: BaseDeleteFieldUseCase): DeleteFieldUseCase
}