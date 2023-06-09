package service
import entity.*
/**
 * GameService class describes the game logic not related to specific player and implements the main game mechanics, such as
 * "start new game", "change to next player" and "calculate winner"
 */
class GameService (private val rootService: RootService) : AbstractRefreshingService() {
    /**
     * Starts a new game with the given list of players and initializes the game's parameters.
     * @property players the list of players who will participate in the game. Must have a size between 2 and 4.
     * @throws IllegalArgumentException if a game already exists
     * @throws IllegalArgumentException when the amount of players is incorrect
     */
    fun startNewGame(players: List<String>) { //how to start game with no input parameters?
        val allCards: MutableList<Card> = defaultRandomCardList() // create general stack
        check(rootService.currentGame == null) { "Game already exist" }
        check(players.size in 2..4) { "Amount of players is incorrect" }
        players.forEach { check(it.isNotEmpty()) { "Name should not be empty!" } }
        allCards.shuffle()
        val playerList = players.map { //creates hand cards for each player
                name ->
            val cards = allCards.slice(0..2).toMutableList()
            allCards.removeAll(cards)
            Player(name, cards)
        }.toMutableList()
        val game = Swim(
            numberOfPasses = 0,
            remainingTurns = 4,
            lastRound = false,
            actPlayer = playerList.first(),
            playerList = playerList,
            midCards = allCards.slice(0..2).toMutableList(), // middle cards get 3 top cards
            deckCards = allCards.drop(3).toMutableList() //deck cards are all unused card of stack
        )
        rootService.currentGame = game
        onAllRefreshables { refreshAfterStartNewGame() }
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
        check(game.remainingTurns > 0) { "Game was over" }
        val players = game.playerList
        val currentIndex = players.indexOf(game.actPlayer)
        val nextIndex = (currentIndex + 1) % players.size
        game.actPlayer = players[nextIndex]
        rootService.currentGame = game
        onAllRefreshables { refreshAfterChangeToNextPerson() }
    }

    /**
     * Calculates the winner od Swim Game based on these rules:
     * for each player we calculate points as biggest sum of one suit
     * if all 3 cards are different get card with the biggest value
     * if 2 cards have the same suit get sum of their values
     * (even smallest SEVEN+EIGHT of one suit are bigger than ACE of another)
     * if all 3 cards have the same suit get 30.5 as the score
     * player with the biggest amount of points wins
     * if there are more than one player with the same amount of points the first one of the playerList wins
     * @throws IllegalArgumentException when game does not exist
     * @throws IllegalArgumentException when we try to calculate winner, but game is still in the process
     * @throws IllegalArgumentException when player somehow has more than 3 cards in hand
     * @throws IllegalArgumentException when somehow player´s score could not be calculated
     * @return names of players and their scores in descending order
     * */
    fun calculateWinner() {
        val game = checkNotNull(rootService.currentGame) { "Current game does not exist" }
        check(game.remainingTurns == 0 || game.deckCards.size < 3) { "Game has not been finished yet" }

        val playerScores = calculatePlayerScores(game) //map of player and his score
        displayWinner(playerScores) //show results
        onAllRefreshables { refreshAfterCalculateWinner() }
    }
    // scores for all players
    private fun calculatePlayerScores(game: Swim): Map<Player, Float> {
        val playerScores = mutableMapOf<Player, Float>() //create map of player and his score
        for (player in game.playerList) { //for every player
            // variable to save maximum score for every single suit
            val maxSameSuitCardsSum = calculateMaxSameSuitCardsSum(player)
            // variable to save final score for every player
            val score = calculatePlayerScore(player, maxSameSuitCardsSum)
            playerScores[player] = score
            player.points = score
        }
        return playerScores
    }
    // maximum score for every suit
    private fun calculateMaxSameSuitCardsSum(player: Player): Float {
        var maxSameSuitCardsSum = 0.0f
        for (suit in CardSuit.values()) { //check through every existing suit
            // list of cards with the same suit
            val suitCards = player.handCards.filter { it.suit == suit }
            when (suitCards.size) { // how many cards of the same suit player has
                0 -> {
                    // nothing happens
                }
                1 -> {
                    val cardSum = getCardValueScore(suitCards[0].value)
                    // saving value of the biggest card if all 3 cards are different
                    if (cardSum > maxSameSuitCardsSum) {
                        maxSameSuitCardsSum = cardSum
                    }
                }
                2 -> {
                    val cardsSum = getCardValueScore(suitCards[0].value) +
                            getCardValueScore(suitCards[1].value)
                    if (cardsSum > maxSameSuitCardsSum) {
                        maxSameSuitCardsSum = cardsSum
                    }
                }
                3 -> {
                    val cardsSum = getCardValueScore(suitCards[0].value) +
                            getCardValueScore(suitCards[1].value) +
                            getCardValueScore(suitCards[2].value)
                    if (cardsSum > maxSameSuitCardsSum) {
                        maxSameSuitCardsSum = cardsSum
                    }
                }
                else -> throw IllegalArgumentException("CardSuit $suit has more than 3 cards")
            }
        }
        return maxSameSuitCardsSum
    }
    // maximum score for each player
    private fun calculatePlayerScore(player: Player, maxSameSuitCardsSum: Float): Float {
        // grouping cards with the same value
        val valueCounts = player.handCards.groupBy { it.value }.values.map { it.size }
        // if player has three cards of the same value
        return if (valueCounts.any { it == 3 }) {
            30.5f
        } else if (maxSameSuitCardsSum > 0) {
            maxSameSuitCardsSum
        } else {
            throw IllegalArgumentException("Score for player $player could not be calculated")
        }
    }
    // shows results
    private fun displayWinner(playerScores: Map<Player, Float>) {
        //sorted list of players based on score
        val sortedPlayerScores = playerScores.toList().sortedByDescending { it.second }
        val winner = sortedPlayerScores.first()
        //winner is the first player (with the biggest score)
        println("The winner is ${winner.first.playerName} with ${winner.second} points")
        //other players with their scores
        for (playerScore in sortedPlayerScores.drop(1)) {
            println("${playerScore.first.playerName} with ${playerScore.second} points")
        }
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