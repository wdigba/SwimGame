package service
import entity.*

/**
 * GameService class describes the game logic not related to specific player and implements the main game mechanics, such as
 * "start new game", "change to next player" and "calculate winner"
 */
class GameService (private val rootService: RootService) {
    /**
     * Starts a new game with the given list of players and initializes the game's parameters.
     * @property playerList the list of players who will participate in the game. Must have a size between 2 and 4.
     * @throws IllegalArgumentException if a game already exists
     * @throws IllegalArgumentException when the amount of players is incorrect
     */
    fun startNewGame(playerList: MutableList<Player>) { //how to start game with no input parameters?
        val allCards: MutableList<Card> = defaultRandomCardList() // create general stack
        check(rootService.currentGame == null) {"Game already exist"}
        check (playerList.size in 2..4) {"Amount of players is incorrect"}
        allCards.shuffled()
        val game = Swim (
            numberOfPasses = 0,
            remainingTurns = 4,
            lastRound = false,
            actPlayer = playerList.first(),
            playerList = playerList,
            midCards = allCards.subList(0,3),
            deckCards = allCards.subList(4,32)
        )
        rootService.currentGame = game
    }
    /**
     * Generates a new list of 32 cards with random suits and values, using the CardSuit and CardValue enums.
     * The generated list is mutable, so the cards can be shuffled or modified.
     * @return a MutableList of Card objects with 32 elements
     */
    private fun defaultRandomCardList() = MutableList(32) { index ->
        Card(
            CardSuit.values()[index / 8], // 32/8 = 4 suits (CLUBS, SPADES, HEARTS, DIAMONDS)
            CardValue.values()[(index % 8) + 5] // values of cards are only from SEVEN to ACE
        )
    }
    /**
     * Changes the active player to the next player in the player list of the current game.
     * @throws IllegalArgumentException when game does not exist
     * @throws IllegalArgumentException when remaining turns of current game are 0
     */
    fun changeToNextPlayer() {
        val game = checkNotNull(rootService.currentGame) { "Current game does not exist" }
        check(game.remainingTurns>0) {"Game was over"}
            val players = game.playerList
            val currentIndex = players.indexOf(game.actPlayer)
            val nextIndex = (currentIndex + 1) % players.size
            game.actPlayer = players[nextIndex]
            rootService.currentGame = game
    }
    /**
     * Calculates the winner od Swim Game based on these rules:
     * for each player we calculate points as biggest sum of one suit
     * if all 3 cards are different get card with the biggest value
     * if 2 cards have the same suit get sum of their values (even smallest SEVEN+EIGHT of one suit are bigger than ACE of another)
     * if all 3 cards have the same suit get 30.5 as the score
     * player with the biggest amount of points wins
     * if there are more than one player with the same amount of points the first one of the playerList wins
     * @throws IllegalArgumentException when game does not exist
     * @throws IllegalArgumentException when we try to calculate winner, but game is still in the process
     * @throws IllegalArgumentException when player somehow has more than 3 cards in hand
     * @throws IllegalArgumentException when somehow player´s score could not be calculated
     * @return name of winner and amount of points
     * */
    fun calculateWinner() {
        val game = checkNotNull(rootService.currentGame) { "Current game does not exist" }
        check (game.remainingTurns>0 && game.deckCards.isNotEmpty()) {"Game has not been finished yet"}

        val playerScores = mutableMapOf<Player, Float>() // create map of player and its score
        for (player in game.playerList) { // for every player
            var score : Float // variable to save score for every player
            var maxSameSuitCardsSum = 0.0f // variable to save maximum score for every single suit
            for (suit in CardSuit.values()) { //check through every existing suit
                val suitCards = player.handCards.filter { it.suit == suit } // list of cards with the same suit
                when (suitCards.size) { // how many cards of the same suit we have
                    0 -> {
                        // nothing happens
                    }
                    1 -> {
                        val cardValueScore = getCardValueScore(suitCards[0].value) // list consists of one card, we get the value for this suit
                        if (cardValueScore > maxSameSuitCardsSum) { // saving value of the biggest card if all 3 cards are different
                            maxSameSuitCardsSum = cardValueScore
                        }
                    }
                    2 -> { //player has 2 cards with the same suit
                        val cardsSum = getCardValueScore(suitCards[0].value) + getCardValueScore(suitCards[1].value) //sum of cards with the same suit
                        if (cardsSum > maxSameSuitCardsSum) { //check for maximum
                            maxSameSuitCardsSum = cardsSum
                        }
                    }
                    3 -> { //if player has 3 cards the same suit
                        maxSameSuitCardsSum = 30.5f
                        break
                    }
                    else -> throw IllegalArgumentException("CardSuit $suit has more than 3 cards")
                }
            }
            score = if (maxSameSuitCardsSum > 0) { // score of player is the biggest sum of the same suit
                maxSameSuitCardsSum
            } else {
                throw IllegalArgumentException ("Score for player $player could not be calculated")
            }
            playerScores[player] = score
        }
        val winner = playerScores.maxByOrNull { it.value }!! //player with the biggest score
        // if scores of players are the same, the winner is the first player in list of players
        println("The winner is ${winner.key.playerName} with ${winner.value} points.")
    }
    /**
     * Receives points for each card based on the rules of the game
     * @throws IllegalArgumentException when cards in stack have value <7
     * */
    private fun getCardValueScore(cardValue: CardValue): Float {
        return when (cardValue) {
            CardValue.ACE -> 11.0f
            CardValue.TEN, CardValue.JACK, CardValue.QUEEN, CardValue.KING -> 10.0f
            CardValue.NINE -> 9.0f
            CardValue.EIGHT -> 8.0f
            CardValue.SEVEN -> 7.0f
            else -> throw IllegalArgumentException("Cards in stack start from 7")
        }
    }

}