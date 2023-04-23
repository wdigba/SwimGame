package entity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
class PlayerTest {

    @Test
    fun testPlayerConstructor() {
        val playerName = "Alice"
        val handCards = mutableListOf(Card(CardSuit.CLUBS, CardValue.TWO), Card(CardSuit.HEARTS, CardValue.EIGHT))
        val points = 10.0

        val player = Player(playerName, handCards, points)

        assertEquals(playerName, player.playerName)
        assertEquals(handCards, player.handCards)
        assertEquals(points, player.points)
    }

    @Test
    fun testPlayerConstructorForNoPoints() {
        val playerName = "Bob"
        val handCards = mutableListOf(Card(CardSuit.SPADES, CardValue.QUEEN))

        val player = Player(playerName, handCards)

        assertEquals(playerName, player.playerName)
        assertEquals(handCards, player.handCards)
    }
}