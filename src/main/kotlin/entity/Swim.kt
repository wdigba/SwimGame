package entity

data class Swim (var numberOfPasses : Int,
            var remainingTurns : Int,
            var lastRound: Boolean,
            var actPlayer: Player,
            var playerList: MutableList<Player>,
            var midCards: MutableList<Card>,
            var deckCards: MutableList<Card>
    ){
}