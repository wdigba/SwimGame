package entity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
/**
This class contains unit tests for the [Player] class
 */
class PlayerTest {
    /**
     * Tests the constructor of the [Player] class.
     * It checks if the suit and value parameters are correctly set when creating a new player with all three parameters
     */
    @Test
    fun testPlayerConstructor() {
        val playerName = "Alice"
        val handCards = mutableListOf(Card(CardSuit.CLUBS, CardValue.TWO), Card(CardSuit.HEARTS, CardValue.EIGHT))
        val points = 10.0f

        val player = Player(playerName, handCards, points)

        assertEquals(playerName, player.playerName)
        assertEquals(handCards, player.handCards)
        assertEquals(points, player.points)
    }
    /**
     * Tests the constructor of the [Player] class.
     * It checks if the suit and value parameters are correctly set when creating a new player with two parameters
     */
    @Test
    fun testPlayerConstructorForNoPoints() {
        val playerName = "Bob"
        val handCards = mutableListOf(Card(CardSuit.SPADES, CardValue.QUEEN))

        val player = Player(playerName, handCards)

        assertEquals(playerName, player.playerName)
        assertEquals(handCards, player.handCards)
    }
}