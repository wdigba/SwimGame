package service

import entity.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
/** Test class to check if two cards (indices given) change their places correctly
 * */
class SwitchOneCardTest {
    /** switch one card without having a current game
     * */
    @Test
    fun `switch one card without current game` () {
        val game = RootService()
        game.currentGame = null
        val expectedErrorMessage = "Current game does not exist"
        val exception = assertThrows(IllegalStateException::class.java) {
            game.playerActionService.switchOneCard(0, 1)
        }
        assertEquals(expectedErrorMessage, exception.message)
    }
    /** switching cards when indices are out of bounds
     * */
    @Test
    fun `switch one card with card out of index` () {
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
        val expectedErrorMessage = "The specified card does not exist!"
        val exception = assertThrows(IllegalStateException::class.java) {
            game.playerActionService.switchOneCard(0, 3)
        }
        assertEquals(expectedErrorMessage, exception.message)
    }
    /** switching cards with valid input
     * */
    @Test
    fun `switch one card with valid input` () {
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
        println ("mid card of index 1" + midCards[1].toString())
        println("hand card of index 2" + handCardsAlice[2].toString())
        game.playerActionService.switchOneCard(1, 2)
        println ("mid card of index 1" + midCards[1].toString())
        println("hand card of index 2" + handCardsAlice[2].toString())
    }
    /**switching one card with random input
     */
    @Test
    fun `switch one with random input`() {
        val game = RootService()
        val playerNames = listOf("Max", "Mike", "Lana")
        game.gameService.startNewGame(playerNames)
        println ("mid card of index 1" + game.currentGame!!.midCards[1].toString())
        println("hand card of index 2" + game.currentGame!!.actPlayer.handCards[2].toString())
        game.playerActionService.switchOneCard(1, 2)
        println ("mid card of index 1" + game.currentGame!!.midCards[1].toString())
        println("hand card of index 2" + game.currentGame!!.actPlayer.handCards[2].toString())
    }
}