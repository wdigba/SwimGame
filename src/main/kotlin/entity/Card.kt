package entity

import kotlin.random.Random
/*Data structure for all Cards in game, provides stack-like construction
* */
data class Card (val suit: CardSuit,
                 val value: CardValue,
                 private val random : Random = Random
){
    private val cards: ArrayDeque<Card> = ArrayDeque(32) //stack of all cards

    val size: Int get() = cards.size //amount of cards in stack
    val empty: Boolean get() = cards.isEmpty() //check if there are enough cards in stack
    fun shuffle() { //shuffle cards in stack
        cards.shuffle(random)
    }

    override fun toString() ="$suit$value"
}