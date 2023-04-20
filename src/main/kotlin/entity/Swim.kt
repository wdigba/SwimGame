package entity

class Swim (var numberOfPasses : Int,
            var remainingTurns : Int,
            var lastRound: Boolean,
            var actPlayer: Player
    ) {
    var playerList: MutableList<Player> = mutableListOf()
    var midCards: MutableList<Card> = mutableListOf()
    var deckCards: MutableList<Card> = mutableListOf()

    fun addPlayer(newPlayer : Player) {
        if (playerList.size>4) throw IllegalArgumentException ("Too much players in this game")
        playerList.add(newPlayer)
    }


}