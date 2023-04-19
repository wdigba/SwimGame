package entity

class Swim (var numberOfPasses : Int,
    var remainingTurns : Int,
    var lastRound: Boolean,
    var actPlayer: Player,
    var playerList: List<Player> = emptyList(),
    var midCards: List<Card> = emptyList(),
    var deckCards: List<Card> = emptyList()
    ) {

}