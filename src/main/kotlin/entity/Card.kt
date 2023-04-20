package entity

import kotlin.random.Random
/*Data structure for all Cards in game, provides stack-like construction
* */
data class Card (val suit: CardSuit,
                 val value: CardValue,
                 private val random : Random = Random
){
    val cards: ArrayDeque<Card> = ArrayDeque(32) //stack of all cards

    val size: Int get() = cards.size //amount of cards in stack
    val empty: Boolean get() = cards.isEmpty() //check if there are enough cards in stack
    fun shuffle() { //shuffle cards in stack
        cards.shuffle(random)
    }
    fun draw(amount: Int = 1): List<Card> //draws required amount of cards from the stack
    {
        require (amount in 1..cards.size) { "can't draw $amount cards from $cards" }
        return List(amount) { cards.removeFirst() }
    }
    fun drawAll(): List<Card> = draw(size) // draws cards from stack
    fun drawOne(): List<Card> = draw(1) // draws exactly one card from stack
    fun drawThree(): List<Card> = draw(3) // draws exactly three cards from stack
    override fun toString() ="$suit$value"
}