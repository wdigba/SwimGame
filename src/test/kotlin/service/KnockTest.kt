package service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
/** Test class to check if knock happens correctly
 * */
class KnockTest {
    /** knocking without having a current game
     * */
    @Test
    fun `pass without current game` () {
        val game = RootService()
        game.currentGame = null
        val expectedErrorMessage = "Current game does not exist"
        val exception = Assertions.assertThrows(IllegalStateException::class.java) {
            game.playerActionService.knock()
        }
        Assertions.assertEquals(expectedErrorMessage, exception.message)
    }
    /** passing when somebody has already knocked
     * */
    @Test
    fun `pass when somebody knocked` () {
        val game = RootService()
        val playerNames = listOf("Max", "Mike", "Lana")
        game.gameService.startNewGame(playerNames)
        game.currentGame!!.lastRound = true
        val expectedErrorMessage = "Someone has already knocked, this is the last round"
        val exception = Assertions.assertThrows(IllegalStateException::class.java) {
            game.playerActionService.knock()
        }
        Assertions.assertEquals(expectedErrorMessage, exception.message)
    }
    /**
     * when player passes correctly
     * changeToNextPlayer expected
     * */
    @Test
    fun `pass valid` () {
        val game = RootService()
        val playerNames = listOf("Max", "Mike", "Lana")
        game.gameService.startNewGame(playerNames)
        println("remaning turns before pass: ${game.currentGame!!.remainingTurns}")
        println("last round status before pass: ${game.currentGame!!.lastRound}")
        game.playerActionService.knock()
        println("remaning turns after pass: ${game.currentGame!!.remainingTurns}")
        println("last round status before pass: ${game.currentGame!!.lastRound}")
    }
}