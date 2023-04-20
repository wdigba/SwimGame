package entity

class Player (val playerName: String,
              var points : Double = 0.0,
              var handCards:MutableList<Card> = mutableListOf()
    ){
    fun addCards(cardStack: MutableList<Card>) { // add cards from stack to player´s hand
        if (cardStack.size<3) throw IllegalArgumentException("Not enough cards in the stack")
        for (i in 1..3) {
            val newCard = cardStack.removeAt(0) //removes card from cardsStack from index 0 and returns it
            handCards.add(newCard)
        }
    }
    fun removeCards() { //remove cards from player´s hand
        handCards.clear()

    }
    fun updatePoints (newPoints: Double) {
        points+=newPoints
    }
}