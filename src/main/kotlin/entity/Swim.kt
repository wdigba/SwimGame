package entity
/**
 * A data class representing a "Swim" game
 * @property numberOfPasses the number of times players have passed during the current round
 * @property remainingTurns the number of turns remaining in the game
 * @property lastRound a boolean indicating whether the current round is the last one
 * @property actPlayer the player whose turn it currently is, as [Player] object
 * @property playerList the list of [Player] objects participating in the game
 * @property midCards the list of [Card] objects, located in the center of the table
 * @property deckCards the list of remaining cards in the deck, as [Card] objects
 * */

data class Swim (
    var numberOfPasses : Int,
    var remainingTurns : Int,
    var lastRound: Boolean,
    var actPlayer: Player,
    var playerList: MutableList<Player>,
    var midCards: MutableList<Card>,
    var deckCards: MutableList<Card>
    ){
    init {
        check(playerList.size in 2..4) {"Amount of players should be between 2 and 4"}
        check (midCards.size == 3)  {"Amount of cards in the center of the table should be 3"}
        check (deckCards.size in 0..32) {"Incorrect amount of cards in stack"}
    }
}