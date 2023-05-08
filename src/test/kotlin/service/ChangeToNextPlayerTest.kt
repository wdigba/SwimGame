package service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
/**Class to check correct order of changing players
 * checks [GameService.changeToNextPlayer]
 * */
class ChangeToNextPlayerTest {
    /**check that current player changes simultaneously and correctly with player of player list
     * */
    @Test
    fun `test ChangeToNextPlayer` () {
        val game = RootService()
        val playerNames = listOf("Max", "Mike", "Lana")
        game.gameService.startNewGame(playerNames)
        assertEquals(game.currentGame!!.actPlayer, game.currentGame!!.playerList.first()) //check for first player
        game.gameService.changeToNextPlayer() //changing the current player in the game
        assertEquals(game.currentGame!!.actPlayer, game.currentGame!!.playerList[1]) //check for second player
        game.gameService.changeToNextPlayer()
        assertEquals(game.currentGame!!.actPlayer, game.currentGame!!.playerList[2]) //change for second player
        game.gameService.changeToNextPlayer()
        assertEquals(game.currentGame!!.actPlayer, game.currentGame!!.playerList.first()) //we change back for the first player in the list
    }
}

