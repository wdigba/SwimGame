package service
import entity.*
class GameService (private val rootService: RootService) {
    fun startNewGame(playerList: MutableList<Player>,
                     allCards: MutableList<Card> = defaultRandomCardList()
    ) {
        check (playerList.size in 2..4) {"Amount of players is incorrect"}
        allCards.shuffled()
        val game = Swim (
            0,
            4,
            false,
            playerList.first(),
            playerList,
            allCards.subList(0,3),
            allCards.subList(4,32)
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

    }
    private fun isGameEnded() : Boolean {
        val game = checkNotNull(rootService.currentGame) {"Current game does not exist"}
        return (game.deckCards.isEmpty() || game.remainingTurns==0)
    }
    /* fun´s frames:
    changeMidCards (when numberofPasses == 4)
    realisation of lastRound? any consequences
    * */
}