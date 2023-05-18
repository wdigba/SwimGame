package service
import entity.*
/**
 * PlayerActionService class describes possible actions of specific player and implements player´s mechanics, such as
 * "switch one card", "switch all cards", "knock" and "pass"
 */
class PlayerActionService (private val rootService: RootService) : AbstractRefreshingService() {
    /** Allows player to reveal cards when the game was started and it´s his turn
     * once cards were revealed, there is no need to reveal them another time
     * */
    fun revealCards() {
        val game = checkNotNull(rootService.currentGame) { "Current game does not exist" }
        val currentPlayer = game.actPlayer
        currentPlayer.cardsRevealed = true
    }
    /**
     * Allows player change one card in hand cards with another card in the middle of the table based on indices
     * @property midCardIndex is an index of middle card that we want to change with one of player´s cards
     * @property playerCardIndex is an index of player´s card that we want to change with one of middle cards
     * @throws IllegalStateException when we can not find required card in the related stack
     * */
    fun switchOneCard(midCardIndex: Int, playerCardIndex: Int){
        val game = checkNotNull(rootService.currentGame) { "Current game does not exist" }
        val currentPlayer = game.actPlayer
        //checks if card exists in the tableCards or in the playerCards
        val midCard = game.midCards.getOrNull(midCardIndex) //check if cards exist
            ?: throw IllegalStateException("The specified card does not exist!")
        val playerCard = currentPlayer.handCards.getOrNull(playerCardIndex)
            ?: throw IllegalStateException("The specified card does not exist!")
        game.midCards[midCardIndex] = playerCard
        currentPlayer.handCards[playerCardIndex] = midCard
        //check for the last round and remaining turns to decide to continue or to finish the game
        if (game.lastRound) {
            game.remainingTurns--
            if (game.remainingTurns == 0) {
                rootService.gameService.calculateWinner()
                return
            }
        }
        onAllRefreshables { refreshAfterSwitchOneCard() }
        rootService.gameService.changeToNextPlayer()
    }
    /**
     * Allows player to change all player´s card with all cards in the middle of the table
     * @throws IllegalArgumentException when game does not exist
     * */
    fun switchAllCards(){
        val game = checkNotNull(rootService.currentGame) { "Current game does not exist" }
        val currentPlayer = game.actPlayer
        val temporaryList = mutableListOf<Card>() //list for temporary storage of cards

        temporaryList.addAll(currentPlayer.handCards)
        currentPlayer.handCards.clear()
        currentPlayer.handCards.addAll(game.midCards)
        game.midCards.clear()
        game.midCards.addAll(temporaryList)
        //check for the last round and remaining turns to decide to continue or to finish the game
        if (game.lastRound) {
            game.remainingTurns--
            if (game.remainingTurns == 0) {
                rootService.gameService.calculateWinner()
                return
            }
        }
        onAllRefreshables { refreshAfterSwitchAllCards() }
        rootService.gameService.changeToNextPlayer()
    }
    /**
     * Allows player to pass and wait for others
     * @throws IllegalArgumentException when game does not exist
     * */
    fun pass() {
        val game = checkNotNull(rootService.currentGame) { "Current game does not exist" }
        game.numberOfPasses++
        //check for the last round and remaining turns to decide to continue or to finish the game
        if (game.lastRound) {
            game.remainingTurns--
            if (game.remainingTurns == 0) {
                rootService.gameService.calculateWinner()
                return
            }
        }
        if(game.numberOfPasses < game.playerList.size) { //not everyone has passed yet
            println("Cannot change mid cards yet, ${game.playerList.size - game.numberOfPasses} pass(es) left")
            rootService.gameService.changeToNextPlayer()
        }
        else {
            changeMidCards()
            rootService.gameService.changeToNextPlayer()
        } //everyone has passed so it´s time to change middle cards
        onAllRefreshables { refreshAfterPass() }
    }
    /**
     * Allows player to knock
     * it means to start "last round", when everyone has one turn to do, till game will be over
     * @throws IllegalArgumentException when game does not exist
     * @throws IllegalArgumentException when someone has already knocked
     * */
    fun knock () {
        val game = checkNotNull(rootService.currentGame) { "Current game does not exist" }
        check (!game.lastRound) { "Someone has already knocked, this is the last round" }
        game.remainingTurns = game.playerList.size - 1
        game.lastRound = true
        onAllRefreshables { refreshAfterKnock() }
        rootService.gameService.changeToNextPlayer()
    }
    /** Extra method to change middle cards
     * after clearing the table, we take first 3 cards from deck and put them on the table
     * @throws IllegalArgumentException when game does not exist
     * */
    private fun changeMidCards() {
        val game = checkNotNull(rootService.currentGame) { "Current game does not exist" }
        /* //irrelevant, because we already prove it in pass()
        if (game.numberOfPasses < game.playerList.size) {
            println("Cannot change mid cards yet, ${game.playerList.size - game.numberOfPasses} pass(es) left")
            return
        } */
        if (game.deckCards.size < 3) {
            println("Not enough cards in deck to change mid cards")
            rootService.gameService.calculateWinner()
            return
        }
        game.midCards.clear()
        game.midCards = game.deckCards.take(3).toMutableList()
        game.deckCards = game.deckCards.drop(3).toMutableList()
        game.numberOfPasses = 0
        rootService.currentGame = game
    }
}