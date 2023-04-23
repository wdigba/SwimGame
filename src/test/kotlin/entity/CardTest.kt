package entity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
class CardTest {
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
    @Test
    fun `toString should return string with suit and value`() {
        val card = Card(CardSuit.SPADES, CardValue.KING)
        assertEquals("♠K", card.toString())
    }

    @Test
    fun `toString should return string with suit and value for ace`() {
        val card = Card(CardSuit.HEARTS, CardValue.ACE)
        assertEquals("♥A", card.toString())
    }
}