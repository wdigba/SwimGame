package service
import entity.*

class PlayerActionService (private val rootService: RootService) {
    fun switchOneCard(midCard: Card, playerCard: Card){
        val game = checkNotNull(rootService.currentGame) { "Current game does not exist" }
        val currentPlayer = game.actPlayer
        //checks if card exists in the tableCards or in the playerCards
        if(midCard !in game.midCards || playerCard !in currentPlayer.handCards){
            throw IllegalStateException("The specified card does not exist!")
        }

        val midCardIndex = game.midCards.indexOf(midCard)
        val playerCardIndex = currentPlayer.handCards.indexOf(playerCard)

        game.midCards[midCardIndex] = playerCard
        currentPlayer.handCards[playerCardIndex] = midCard

        game.numberOfPasses = 0
        rootService.gameService.changeToNextPlayer()
    }
    fun switchAllCards(){
        val game = checkNotNull(rootService.currentGame) { "Current game does not exist" }
        val currentPlayer = game.actPlayer
        val temporaryList = mutableListOf<Card>()
        temporaryList.addAll(currentPlayer.handCards)
        currentPlayer.handCards.clear()
        currentPlayer.handCards.addAll(game.midCards)
        game.midCards.clear()
        game.midCards.addAll(temporaryList)

        game.numberOfPasses = 0
        rootService.gameService.changeToNextPlayer()
    }
    fun pass() {
        val game = checkNotNull(rootService.currentGame) { "Current game does not exist" }
        game.numberOfPasses++
        if(game.numberOfPasses < game.playerList.size) {
            rootService.gameService.changeToNextPlayer()
        }
        else rootService.gameService.changeMidCards()
    }
    fun knock () {
        val game = checkNotNull(rootService.currentGame) { "Current game does not exist" }
        //how to check if someone has already knocked? somehow we have to remember which player has knocked, so we could give turn to next
        //players until that one person
        rootService.gameService.changeToNextPlayer()
    }

}