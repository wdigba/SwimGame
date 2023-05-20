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
        assertEquals(game.currentGame!!.actPlayer, game.currentGame!!.playerList.first())
        //we change back for the first player in the list
    }
    /** trying to change the player when no game was started
     * */
    @Test
    fun `ChangeToNextPlayer without game`() {
        val game = RootService()
        game.currentGame = null
        val expectedErrorMessage = "Current game does not exist"
        val exception = assertThrows(IllegalStateException::class.java) {
            game.gameService.changeToNextPlayer()
        }
        assertEquals(expectedErrorMessage, exception.message)
    }
    @Test
    fun `ChangeToNextPlayer when game was over`() {
        val game = RootService()
        val playerNames = listOf("Max", "Mike", "Lana")
        game.gameService.startNewGame(playerNames)
        val expectedErrorMessage = "Game was over"
        game.currentGame!!.remainingTurns = 0
        val exception = assertThrows(IllegalStateException::class.java) {
            game.gameService.changeToNextPlayer()
        }
        assertEquals(expectedErrorMessage, exception.message)
    }
}

