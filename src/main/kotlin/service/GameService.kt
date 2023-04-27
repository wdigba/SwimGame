package service
import entity.*

class GameService (private val rootService: RootService) {
    fun startNewGame(playerList: MutableList<Player>,
                     allCards: MutableList<Card> = defaultRandomCardList()
    ) {
        check(rootService.currentGame == null) {"Game already exist"}
        check (playerList.size in 2..4) {"Amount of players is incorrect"}
        allCards.shuffled()
        val game = Swim (
            numberOfPasses = 0,
            remainingTurns = 4,
            lastRound = false,
            actPlayer = playerList.first(),
            playerList = playerList,
            midCards = allCards.subList(0,3),
            deckCards = allCards.subList(4,32)
        )
        rootService.currentGame = game
    }
    private fun defaultRandomCardList() = MutableList(32) { index ->
        Card(
            CardSuit.values()[index / 8],
            CardValue.values()[(index % 8) + 5]
        )
    }
    fun changeToNextPlayer() {
        val game = checkNotNull(rootService.currentGame) { "Current game does not exist" }
        val players = game.playerList
        val currentIndex = players.indexOf(game.actPlayer)
        val nextIndex = (currentIndex + 1) % players.size
        game.actPlayer = players[nextIndex]
        rootService.currentGame = game
    }
    fun calculateWinner() {
        val game = checkNotNull(rootService.currentGame) {"Current game does not exist"}
        if (!isGameEnded()) {
            println("Game has not been finished yet")
            return
        }

        val playerScores = mutableMapOf<Player, Float>()
        for (player in game.playerList) {
            var score = 0.0f
            var suitScore = 0.0f
            for (suit in CardSuit.values()) {
                val suitCards = player.handCards.filter { it.suit == suit }
                if (suitCards.size == 3){
                    suitScore = 30.5f
                    break
                }
                else if (suitCards.isNotEmpty()) {
                    for (card in suitCards) {
                        suitScore += when (card.value) {
                            CardValue.ACE -> 11.0f
                            CardValue.TEN, CardValue.JACK, CardValue.QUEEN, CardValue.KING -> 10.0f
                            CardValue.NINE -> 9.0f
                            CardValue.EIGHT -> 8.0f
                            CardValue.SEVEN -> 7.0f
                            else -> throw IllegalArgumentException("Cards in stack start from 7")
                        }
                    }
                }
                check (suitScore <= 31.0) {"Amount of points is incorrect"}
                score += suitScore
            }
            playerScores[player] = score
        }
        val winner = playerScores.maxByOrNull { it.value }!!
        println("The winner is ${winner.key.playerName} with ${winner.value} points.")
        //realisation is not fully correct, it does not count points correctly, in case all suits are different it gives sum of these cards back,
        // but it has to give value of the biggest card instead
    }
    private fun isGameEnded() : Boolean {
        val game = checkNotNull(rootService.currentGame) {"Current game does not exist"}
        return (game.deckCards.isEmpty() || game.remainingTurns==0)
    }
    fun changeMidCards() {
        val game = checkNotNull(rootService.currentGame) { "Current game does not exist" }
        if (game.numberOfPasses < game.playerList.size) {
            println("Cannot change mid cards yet, ${4 - game.numberOfPasses} pass(es) left")
            return
        }
        if (game.deckCards.size < 3) {
            println("Not enough cards in deck to change mid cards")
            return
            //end game??
        }
        game.midCards = game.deckCards.take(3).toMutableList()
        game.deckCards = game.deckCards.drop(3).toMutableList()
        game.numberOfPasses = 0
        rootService.currentGame = game
    }
    /*
    realisation of lastRound? any consequences
    * */
}