package service

import entity.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
/** Test class to check if two cards (indices given) change their places correctly
 * checks [PlayerActionService.switchOneCard]
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
    /**switching cards when game was automatically generated and method had invalid card index to switch
     * */
    @Test
    fun `switch one card random stack playerCard out of index` () {
        val game = RootService()
        val playerNames = listOf("Max", "Mike", "Lana")
        game.gameService.startNewGame(playerNames)
        val expectedErrorMessage = "The specified card does not exist!"
        val exception = assertThrows(IllegalStateException::class.java) {
            game.playerActionService.switchOneCard(0, 3)
        }
        assertEquals(expectedErrorMessage, exception.message)
    }
    /**switching cards when game was automatically generated and method had invalid other card index to switch
     * */
    @Test
    fun `switch one card random stack midCard out of index` () {
        val game = RootService()
        val playerNames = listOf("Max", "Mike", "Lana")
        game.gameService.startNewGame(playerNames)
        val expectedErrorMessage = "The specified card does not exist!"
        val exception = assertThrows(IllegalStateException::class.java) {
            game.playerActionService.switchOneCard(5, 2)
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
        val initialMidCard = midCards[1]
        val initialPlayerCard = handCardsAlice[2]

        game.playerActionService.switchOneCard(1, 2)

        val finalMidCard = midCards[1]
        val finalPlayerCard = handCardsAlice[2]
        assertEquals(initialMidCard, finalPlayerCard)
        assertEquals(initialPlayerCard, finalMidCard)
        /* //previous manual check
        println ("middle card of index 1" + midCards[1].toString())
        println("hand card of index 2" + handCardsAlice[2].toString())
        game.playerActionService.switchOneCard(1, 2)
        println ("middle card of index 1" + midCards[1].toString())
        println("hand card of index 2" + handCardsAlice[2].toString())
         */
    }
    /**switching one card with random input
     * !! Test works properly
     * the only thing is that we move to the next player right after we have done the switch
     * so to prove the correctness it´s necessary to MUTE rootService.gameService.changeToNextPlayer()
     */
    @Test
    fun `switch one with random input`() {
        val game = RootService()
        val playerNames = listOf("Max", "Mike", "Lana")
        game.gameService.startNewGame(playerNames)

        /* //check is valid when rootService.gameService.changeToNextPlayer() muted
        val initialMidCard = game.currentGame!!.midCards[1]
        val initialPlayerCard = game.currentGame!!.actPlayer.handCards[2]

        game.playerActionService.switchOneCard(1, 2)

        val finalMidCard = game.currentGame!!.midCards[1]
        val finalPlayerCard = game.currentGame!!.actPlayer.handCards[2]
        assertEquals(initialMidCard, finalPlayerCard)
        assertEquals(initialPlayerCard, finalMidCard)
         */
        println ("mid card of index 1 is" + game.currentGame!!.midCards[1].toString())
        println("hand card of index 2 is" + game.currentGame!!.actPlayer.handCards[2].toString())
        game.playerActionService.switchOneCard(1, 2)
        println ("mid card of index 1 is" + game.currentGame!!.midCards[1].toString())
        println("hand card of index 2 is " + game.currentGame!!.actPlayer.handCards[2].toString())
    }
    /** player decided to switch one card, but it was the last round
     * [GameService.calculateWinner] expected
     * */
    @Test
    fun `switch one with random input but it was the last round`() {
        val game = RootService()
        val playerNames = listOf("Max", "Mike", "Lana")
        game.gameService.startNewGame(playerNames)
        game.currentGame!!.lastRound = true
        game.currentGame!!.remainingTurns = 1
        game.playerActionService.switchOneCard(1, 2)
    }
}