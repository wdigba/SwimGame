package service

import entity.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
/** Test class to check if pass happens correctly
 * */
class PassTest {
    /** passing without having a current game
     * */
    @Test
    fun `pass without current game` () {
        val game = RootService()
        game.currentGame = null
        val expectedErrorMessage = "Current game does not exist"
        val exception = Assertions.assertThrows(IllegalStateException::class.java) {
            game.playerActionService.pass()
        }
        Assertions.assertEquals(expectedErrorMessage, exception.message)
    }
    /** passing when it was last round and remaining turns 0
     * calculateWinner expected
     * */
    @Test
    fun `pass when last round and remaning turns 0` () {
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
            remainingTurns = 1,
            lastRound = true,
            actPlayer = player1,
            playerList = playerList,
            midCards = midCards,
            deckCards = deckCards
        )
        game.currentGame = swim
        game.playerActionService.pass()
    }
    /** passing when it is not the last round, random generated
     * changeToNextPlayer() expected
     * */
    @Test
    fun `pass when not every one has passed random generated` () {
        val game = RootService()
        val playerNames = listOf("Max", "Mike", "Lana")
        game.gameService.startNewGame(playerNames)
        println("current player: ${game.currentGame!!.actPlayer.toString()}")
        game.playerActionService.pass()
        println("current player: ${game.currentGame!!.actPlayer.toString()}")
    }
    /** passing when everyone has already passed
     * changeMidCards() expected
     * !! DID NOT COMPLETELY COVER ALL TESTS OF CHANGE MID CARDS
     * */
    @Test
    fun `pass when everyone passed` () {
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
            Card(CardSuit.DIAMONDS, CardValue.QUEEN)
        )
        val deckCards = mutableListOf(
            Card(CardSuit.CLUBS, CardValue.TEN),
            Card(CardSuit.SPADES, CardValue.SEVEN),
            Card(CardSuit.HEARTS, CardValue.JACK),
        )
        val swim = Swim(
            numberOfPasses = 2,
            remainingTurns = 4,
            lastRound = false,
            actPlayer = player1,
            playerList = playerList,
            midCards = midCards,
            deckCards = deckCards
        )
        game.currentGame = swim
        println("mid cards: ${game.currentGame!!.midCards.joinToString("; ")}")
        println("deck cards: ${game.currentGame!!.deckCards.joinToString("; ")}")
        game.playerActionService.pass()
        println("mid cards: ${game.currentGame!!.midCards.joinToString("; ")}")
    }
}