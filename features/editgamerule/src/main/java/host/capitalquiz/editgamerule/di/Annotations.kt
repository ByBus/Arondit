package host.capitalquiz.editgamerule.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class EditMode

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class CreationMode