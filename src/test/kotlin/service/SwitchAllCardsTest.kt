package service

import entity.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
/** Test class to check if all cards (middle and hand) change their places correctly
 * */
class SwitchAllCardsTest {
    /** switch one card without having a current game
     * */
    @Test
    fun `switch all cards without current game` () {
        val game = RootService()
        game.currentGame = null
        val expectedErrorMessage = "Current game does not exist"
        val exception = assertThrows(IllegalStateException::class.java) {
            game.playerActionService.switchAllCards()
        }
        assertEquals(expectedErrorMessage, exception.message)
    }
    /** switching cards with valid input
     * */
    @Test
    fun `switch all cards with valid input` () {
        val game = RootService()
        val handCardsAlice = mutableListOf(
            Card(CardSuit.SPADES, CardValue.QUEEN),
            Card(CardSuit.DIAMONDS, CardValue.JACK),
            Card(CardSuit.SPADES, CardValue.ACE)
        )
        val handCardsBob = mutableListOf(
            Card(CardSuit.HEARTS, CardValue.TEN),
            Card(CardSuit.SPADES, CardValue.JACK),
            Card(CardSuit.CLUBS, CardValue.QUEEN)
        )
        val player1 = Player("Alice", handCardsAlice)
        val player2 = Player("Bob", handCardsBob)
        val playerList = mutableListOf(player1, player2)
        val midCards = mutableListOf(
            Card(CardSuit.SPADES, CardValue.ACE),
            Card(CardSuit.HEARTS, CardValue.KING),
            Card(CardSuit.CLUBS, CardValue.QUEEN)
        )
        val deckCards = mutableListOf(
            Card(CardSuit.CLUBS, CardValue.TEN)
        )
        val swim = Swim(
            numberOfPasses = 0,
            remainingTurns = 4,
            lastRound = false,
            actPlayer = player1,
            playerList = playerList,
            midCards = midCards,
            deckCards = deckCards
        )
        game.currentGame = swim
        println("mid cards: ${midCards.joinToString("; ")}")
        println("hand cards: ${handCardsAlice.joinToString("; ")}")
        game.playerActionService.switchAllCards()
        println("mid cards: ${midCards.joinToString("; ")}")
        println("hand cards: ${handCardsAlice.joinToString("; ")}")
    }
    /** switching all cards with random input
     * !! DOES NOT WORK PROPERLY
     * */
    @Test
    fun `switch all with random input`() {
        val game = RootService()
        val playerNames = listOf("Max", "Mike", "Lana")
        game.gameService.startNewGame(playerNames)
        println("mid cards: ${game.currentGame!!.midCards.joinToString("; ")}")
        println("hand cards: ${game.currentGame!!.actPlayer.handCards.joinToString("; ")}")
        game.playerActionService.switchAllCards()
        println("mid cards: ${game.currentGame!!.midCards.joinToString("; ")}")
        println("hand cards: ${game.currentGame!!.actPlayer.handCards.joinToString("; ")}")
    }
}