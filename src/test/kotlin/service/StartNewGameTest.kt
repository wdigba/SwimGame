package service

import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import org.junit.jupiter.api.Assertions.*

/**
 * Class that provides tests for starting new game
 * checks [GameService.startNewGame]
 * */
class StartNewGameTest {
    /** general test to check existence and input
     * */
    @Test
    fun `test startNewGame general`(){
        val game = RootService()
        val playerNames = listOf("Max", "Mike", "Lana")
        val invalidPlayerAmount = listOf("Bob", "Britney", "Billy", "Brian", "Bart")
        assertTrue(game.currentGame == null) //game before start
        game.gameService.startNewGame(playerNames) //game created
        assertTrue(game.currentGame != null) //game exists
        //start new game but game exists
        assertFailsWith<IllegalStateException> { game.gameService.startNewGame(playerNames) }
        game.currentGame = null
        //start new game with invalid amount of players
        assertFailsWith<IllegalStateException> { game.gameService.startNewGame(invalidPlayerAmount) }
    }
    /** test with correct input
     * */
    @Test
    fun `test startNewGame with valid input`() {
        val game = RootService()
        val playerNames = listOf("Alice", "Bob", "Charlie")
        assertTrue(game.currentGame == null)
        game.gameService.startNewGame(playerNames)
        assertTrue(game.currentGame != null)
        assertEquals(3, game.currentGame!!.playerList.size)
        assertEquals(4, game.currentGame!!.remainingTurns)
        assertEquals(false, game.currentGame!!.lastRound)
        assertEquals("Alice", game.currentGame!!.actPlayer.playerName)
        assertEquals(3, game.currentGame!!.midCards.size)
        // we create 32 cards in stack, 3*3 goes to players, 3 goes to middle
        assertEquals(20, game.currentGame!!.deckCards.size)
        //=> 32-9-3 = 20 cards left for deck
    }
    /** test with not enough players
     * */
    @Test
    fun `test startNewGame with too few players`() {
        val game = RootService()
        val exception = assertThrows(IllegalStateException::class.java) {
            game.gameService.startNewGame(listOf("Alice"))
        }
        assertEquals("Amount of players is incorrect", exception.message)
        assertNull(game.currentGame)
    }
    /** test with too many players
     * */
    @Test
    fun `test startNewGame with too many players`() {
        val game = RootService()
        val exception = assertThrows(IllegalStateException::class.java) {
            game.gameService.startNewGame(listOf("Alice", "Bob", "Charlie", "David", "Eve"))
        }
        assertEquals("Amount of players is incorrect", exception.message)
        assertNull(game.currentGame)
    }
    /** test with a player with no name
     * */
    @Test
    fun `test startNewGame with empty player name`() {
        val game = RootService()
        val exception = assertThrows(IllegalStateException::class.java) {
            game.gameService.startNewGame(listOf("Alice", "", "Charlie"))
        }
        assertEquals("Name should not be empty!", exception.message)
        assertNull(game.currentGame)
    }
    /** test when game already exist
     * */
    @Test
    fun `test startNewGame when game already exists`() {
        val game = RootService()
        game.gameService.startNewGame(listOf("Alice", "Bob", "Charlie"))
        val exception = assertThrows(IllegalStateException::class.java) {
            game.gameService.startNewGame(listOf("David", "Eve"))
        }
        assertEquals("Game already exist", exception.message)
        assertNotNull(game.currentGame)
    }
}
