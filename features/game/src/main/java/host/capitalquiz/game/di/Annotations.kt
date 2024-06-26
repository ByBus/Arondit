package host.capitalquiz.game.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class OzhegovGlossary

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class GufoMeGlossary

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class CompositeGlossary