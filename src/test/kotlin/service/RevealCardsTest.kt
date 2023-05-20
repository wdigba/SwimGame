package service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
/** test to correctness of usage [PlayerActionService.revealCards]
 * */
class RevealCardsTest {
    /** reveal cards when actual game does not exist yet
     * */
    @Test
    fun `reveal cards without current game` () {
        val game = RootService()
        game.currentGame = null
        val expectedErrorMessage = "Current game does not exist"
        val exception = Assertions.assertThrows(IllegalStateException::class.java) {
            game.playerActionService.revealCards()
        }
        Assertions.assertEquals(expectedErrorMessage, exception.message)
    }
    /** reveal cards for an actual player
     * */
    @Test
    fun `reveal cards results for actual player` () {
        val game = RootService()
        val playerNames = listOf("Max", "Mike", "Lana")
        game.gameService.startNewGame(playerNames)
        assertEquals(game.currentGame!!.actPlayer.cardsRevealed, false)
        game.playerActionService.revealCards()
        assertEquals(game.currentGame!!.actPlayer.cardsRevealed, true)
    }
}