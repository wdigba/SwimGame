package entity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
class SwimTest {

    @Test
    fun testSwimConstructor() {
        val player1 = Player("Alice", mutableListOf())
        val player2 = Player("Bob", mutableListOf())
        val playerList = mutableListOf(player1, player2)
        val deckCards = mutableListOf(
            Card(CardSuit.SPADES, CardValue.ACE),
            Card(CardSuit.HEARTS, CardValue.KING),
            Card(CardSuit.CLUBS, CardValue.QUEEN)
        )
        val swim = Swim(
            numberOfPasses = 0,
            remainingTurns = 10,
            lastRound = false,
            actPlayer = player1,
            playerList = playerList,
            midCards = mutableListOf(),
            deckCards = deckCards
        )

        assertEquals(0, swim.numberOfPasses)
        assertEquals(10, swim.remainingTurns)
        assertEquals(false, swim.lastRound)
        assertEquals(player1, swim.actPlayer)
        assertEquals(playerList, swim.playerList)
        assertEquals(mutableListOf<Card>(), swim.midCards)
        assertEquals(deckCards, swim.deckCards)
    }
    @Test
    fun testSwimConstructorSecond() {
        val player1 = Player("Mary", mutableListOf())
        val player2 = Player("Peter", mutableListOf())
        val playerList = mutableListOf(player1, player2)
        val deckCards = mutableListOf(
            Card(CardSuit.DIAMONDS, CardValue.SIX),
            Card(CardSuit.DIAMONDS, CardValue.JACK),
            Card(CardSuit.HEARTS, CardValue.TEN)
        )
        val midCards = mutableListOf(
            Card(CardSuit.CLUBS, CardValue.ACE),
            Card(CardSuit.HEARTS, CardValue.FIVE),
            Card(CardSuit.SPADES, CardValue.THREE)
        )
        val swim = Swim(
            numberOfPasses = 2,
            remainingTurns = 3,
            lastRound = true,
            actPlayer = player2,
            playerList = playerList,
            midCards = midCards,
            deckCards = deckCards
        )

        assertEquals(2, swim.numberOfPasses)
        assertEquals(3, swim.remainingTurns)
        assertEquals(true, swim.lastRound)
        assertEquals(player2, swim.actPlayer)
        assertEquals(playerList, swim.playerList)
        assertEquals(midCards, swim.midCards)
        assertEquals(deckCards, swim.deckCards)
    }
}