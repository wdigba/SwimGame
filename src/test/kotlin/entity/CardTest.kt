package entity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
/**
This class contains unit tests for the [Card] class
 */
class CardTest {
    /**
     * Tests the constructor of the [Card] class.
     * It checks if the suit and value parameters are correctly set when creating a new card
     */
    @Test
    fun testConstructor() {
        val card1 = Card(CardSuit.HEARTS, CardValue.ACE)
        assertEquals(CardSuit.HEARTS, card1.suit)
        assertEquals(CardValue.ACE, card1.value)

        val card2 = Card(CardSuit.SPADES, CardValue.KING)
        assertEquals(CardSuit.SPADES, card2.suit)
        assertEquals(CardValue.KING, card2.value)

        val card3 = Card(CardSuit.DIAMONDS, CardValue.TWO)
        assertEquals(CardSuit.DIAMONDS, card3.suit)
        assertEquals(CardValue.TWO, card3.value)
    }
    /**
     * Tests the toString() method of the [Card] class.
     * It checks if the string returned by toString() contains the correct suit and value of a Spades King card
     */
    @Test
    fun `toString should return string with suit and value`() {
        val card = Card(CardSuit.SPADES, CardValue.KING)
        assertEquals("♠K", card.toString())
    }
    /**
     * Tests the toString() method of the [Card] class.
     * It checks if the string returned by toString() contains the correct suit and value of a Hearts Ace card
     */
    @Test
    fun `toString should return string with suit and value for ace`() {
        val card = Card(CardSuit.HEARTS, CardValue.ACE)
        assertEquals("♥A", card.toString())
    }
}