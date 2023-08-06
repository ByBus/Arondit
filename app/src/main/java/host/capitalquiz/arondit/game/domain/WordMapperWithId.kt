package host.capitalquiz.arondit.game.domain

interface WordMapperWithId<ID, R> : WordMapper<R>{
    var additionalId: ID
}