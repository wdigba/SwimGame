package entity

import kotlin.random.Random

class Swim (var numberOfPasses : Int,
            var remainingTurns : Int,
            var lastRound: Boolean,
            var actPlayer: Player,
            private val random : Random = Random
    ) {
    var playerList: MutableList<Player> = mutableListOf()
    var midCards: MutableList<Card> = mutableListOf()
    //var deckCards: MutableList<Card> = mutableListOf()
    private val cards: ArrayDeque<Card> = ArrayDeque(32) //stack of all cards

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
    fun drawThree(): List<Card> = draw(3) // draws exactly three cards from stack

    fun addPlayer(newPlayer : Player) {
        if (playerList.size>4) throw IllegalArgumentException ("Too much players in this game")
        playerList.add(newPlayer)
    }
    fun addMidCards() {
        midCards.addAll(drawThree())
    }
    fun removeMidCards() = midCards.clear()

    //fun addDeckCards(){ deckCards.addAll(drawAll()) }

    fun timeToChangeMidCards() : Boolean {
       return  numberOfPasses == playerList.size
    }
    fun changeMidCards () {
        if (timeToChangeMidCards()) {
            removeMidCards()
            addMidCards()
        }
    }
}