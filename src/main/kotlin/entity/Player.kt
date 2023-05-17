package entity
/**
 * Data class representing a player in a card game
 * @property playerName the name of the player
 * @property handCards a mutable list of cards, with [Card] as an object, held by the player
 * @property points the current point total of the player
 * */
data class Player (
    val playerName: String,
    var handCards:MutableList<Card>,
    var points : Float = 0.0f,
    var cardsRevealed: Boolean = false
    ){
    init {
        check(playerName != "") {"Name cannot be empty"}
        check(handCards.size == 3) {"Amount of cards in hand should be 3"}
    }
}