package service

import entity.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.lang.IllegalArgumentException

/** test to calculate winner based on game rules
 * checks [GameService.calculateWinner]
 * */
class CalculateWinnerTest {
    /** there is no game to calculate the winner
     * */
    @Test
    fun `test calculateWinner with no game`() {
        val game = RootService()
        game.currentGame = null
        val expectedErrorMessage = "Current game does not exist"
        val exception = assertThrows(IllegalStateException::class.java) {
            game.gameService.calculateWinner()
        }
        assertEquals(expectedErrorMessage, exception.message)
    }
    /**game was not finish, so we can not calculate the winner
     * */
    @Test
    fun `test calculateWinner before game finished`() {
        val game = RootService()
        val playerNames = listOf("Max", "Mike", "Lana")
        game.gameService.startNewGame(playerNames)
        val expectedErrorMessage = "Game has not been finished yet"
        val exception = assertThrows(IllegalStateException::class.java) {
            game.gameService.calculateWinner()
        }
        assertEquals(expectedErrorMessage, exception.message)
    }
    /** result with random automation generation of cards by game
     * */
    @Test
    fun `test calculateWinner with winner random`() {
        val game = RootService()
        val playerNames = listOf("Max", "Lana", "Mike")
        game.gameService.startNewGame(playerNames)
        game.currentGame!!.remainingTurns = 0
        game.gameService.calculateWinner()
    }
    /** result with defined cards of players
     * usage of assertion is impossible, because calculateWinner returns type Unit
     * check is manual
     * */
    @Test
    fun `test calculateWinner with defined hand cards` () {
        val game = RootService()
        //create game manually
        val handCardsAlice = mutableListOf(
            Card(CardSuit.SPADES, CardValue.QUEEN),
            Card(CardSuit.DIAMONDS, CardValue.JACK),
            Card(CardSuit.SPADES, CardValue.ACE))
        val handCardsBob = mutableListOf(
            Card(CardSuit.HEARTS, CardValue.TEN),
            Card(CardSuit.SPADES, CardValue.TEN),
            Card(CardSuit.CLUBS, CardValue.TEN))
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
            remainingTurns = 0,
            lastRound = false,
            actPlayer = player1,
            playerList = playerList,
            midCards = midCards,
            deckCards = deckCards
        )
        game.currentGame = swim
        println("The winner is Bob with 30.5 points\nAlive with 21.0 points") //expected
        game.gameService.calculateWinner() //actual
    }
    /**result with defined cards of players
     * usage of assertion is impossible, because calculateWinner returns type Unit
     * players have the same score 20
     * */
    @Test
    fun `test calculateWinner with defined hand cards where two players have same score` () {
        val game = RootService()
        //create game manually
        val handCardsAlice = mutableListOf(
            Card(CardSuit.SPADES, CardValue.QUEEN),
            Card(CardSuit.DIAMONDS, CardValue.JACK),
            Card(CardSuit.SPADES, CardValue.JACK))
        val handCardsBob = mutableListOf(
            Card(CardSuit.CLUBS, CardValue.TEN),
            Card(CardSuit.SPADES, CardValue.JACK),
            Card(CardSuit.CLUBS, CardValue.QUEEN))
        val player1 = Player("Alice", handCardsAlice)
        val player2 = Player("Bob", handCardsBob)
        val playerList = mutableListOf(player1, player2)
        val midCards = mutableListOf(
            Card(CardSuit.SPADES, CardValue.SEVEN),
            Card(CardSuit.HEARTS, CardValue.EIGHT),
            Card(CardSuit.CLUBS, CardValue.TEN)
        )
        val deckCards = mutableListOf(
            Card(CardSuit.CLUBS, CardValue.EIGHT)
        )
        val swim = Swim(
            numberOfPasses = 0,
            remainingTurns = 0,
            lastRound = false,
            actPlayer = player1,
            playerList = playerList,
            midCards = midCards,
            deckCards = deckCards
        )
        game.currentGame = swim
        println("The winner is Alice with 20.0 points\nBob with 20.0 points") //expected
        game.gameService.calculateWinner() //actual
        //the result is given in the same order as players in player list
    }
    @Test
    fun `test calculateWinner with defined hand cards where CardValue is wrong` () {
        val game = RootService()
        //create game manually
        val handCardsAlice = mutableListOf(
            Card(CardSuit.SPADES, CardValue.SIX),
            Card(CardSuit.DIAMONDS, CardValue.JACK),
            Card(CardSuit.SPADES, CardValue.JACK))
        val handCardsBob = mutableListOf(
            Card(CardSuit.CLUBS, CardValue.TEN),
            Card(CardSuit.SPADES, CardValue.JACK),
            Card(CardSuit.CLUBS, CardValue.QUEEN))
        val player1 = Player("Alice", handCardsAlice)
        val player2 = Player("Bob", handCardsBob)
        val playerList = mutableListOf(player1, player2)
        val midCards = mutableListOf(
            Card(CardSuit.SPADES, CardValue.SEVEN),
            Card(CardSuit.HEARTS, CardValue.EIGHT),
            Card(CardSuit.CLUBS, CardValue.TEN)
        )
        val deckCards = mutableListOf(
            Card(CardSuit.CLUBS, CardValue.EIGHT)
        )
        val swim = Swim(
            numberOfPasses = 0,
            remainingTurns = 0,
            lastRound = false,
            actPlayer = player1,
            playerList = playerList,
            midCards = midCards,
            deckCards = deckCards
        )
        game.currentGame = swim
        val expectedErrorMessage = "Cards in stack start from 7"
        val exception = assertThrows(IllegalArgumentException::class.java) {
            game.gameService.calculateWinner()
        }
        assertEquals(expectedErrorMessage, exception.message)
    }
}