package view

import entity.*
import service.*
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.container.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.util.Font
import java.awt.Color
import tools.aqua.bgw.visual.CompoundVisual

/**[BoardGameScene] provides representation of the game itself
 * all players keep their hand cards and located at the edges of the table
 * stack and middle cards, that can be used, located in the center
 * at first all cards of a player are unrevealed
 * with [revealCards] player can open hand cards and do some actions
 * before the hand cards are revealed, no action is available
 * with [switchAllButton] player can change all hand cards with middle cards
 * with [switchOneButton] player can change one card from hand with one card from the middle
 * with [passButton] player can pass
 * with [knockButton] player can knock to finish the game
 * "swim game" finishes automatically when one of the termination conditions was reached
 * in particular: when stack ran out of cards
 * or the last player of the last round has made an action
 * */
class SwimGameScene(private val rootService: RootService):
    BoardGameScene(1920, 1080), Refreshable {
    private var playerAmount = rootService.currentGame?.playerList?.size
    // layouts for all cards on the table
    private var layouts = mutableListOf<LinearLayout<CardView>>()
    // labels for all names on the table
    private var nameLabels = mutableListOf<Label>()
    // index of one of hand cards that will be changed
    private var playerCardIndex = 0
    // index of one of table cards that will be changed
    private var midCardIndex = 0
    //(String)PlayerHandLayout - layout for hand cards of each player
    //(String)PlayerLabel - name of each player
    private var currentPlayerHandLayout: LinearLayout<CardView> = LinearLayout(
        height = 220,
        width = 600,
        posX = 650,
        posY = 800,
        spacing = 30,
        alignment = Alignment.CENTER
    )

    private var currentPlayerLabel = Label(
        width = 200,
        height = 50,
        posX = 850,
        posY = 740,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    private var topPlayerHandLayout: LinearLayout<CardView> = LinearLayout(
        height = 220,
        width = 600,
        posX = 650,
        posY = 50,
        spacing = 30,
        alignment = Alignment.CENTER
    )

    private var topPlayerLabel = Label(
        width = 200,
        height = 50,
        posX = 850,
        posY = 280,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    private var leftPlayerHandLayout: LinearLayout<CardView> = LinearLayout(
        height = 220,
        width = 600,
        posX = -100,
        posY = 425,
        spacing = 30,
        alignment = Alignment.CENTER
    )

    private var leftPlayerLabel = Label(
        width = 200,
        height = 50,
        posX = 100,
        posY = 250,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )

    private var rightPlayerHandLayout: LinearLayout<CardView> = LinearLayout(
        height = 220,
        width = 600,
        posX = 1410,
        posY = 425,
        spacing = 30,
        alignment = Alignment.CENTER
    )

    private var rightPlayerLabel = Label(
        width = 200,
        height = 50,
        posX = 1610,
        posY = 250,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )
    // available for actions cards in the middle of the table
    private var midCardsLayout: LinearLayout<CardView> = LinearLayout(
        height = 220,
        width = 600,
        posX = 650,
        posY = 425,
        spacing = 30,
        alignment = Alignment.CENTER
    )
    // visual representation of the stack
    private var cardStackLayout: CardStack<CardView> = CardStack(
        height = 220,
        width = 150,
        posX = 1200,
        posY = 425,
        alignment = Alignment.CENTER
    )
    // action buttons
    // allows to switch all cards
    private val switchAllButton = Button(
        height = 70,
        width = 250,
        posX = 100,
        posY = 940,
        text = "switch all",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 28)
    ).apply {
        onMouseClicked = {
            rootService.playerActionService.switchAllCards()
        }
    }
    // allows to switch one card
    private val switchOneButton = Button(
        height = 70,
        width = 250,
        posX = 380,
        posY = 940,
        text = "switch one",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 28)
    ).apply {
        onMouseClicked = {
            // deletes other action buttons from the table
            removeComponents(this, knockButton, switchAllButton, passButton)
            // call to click on hand card
            tableTextLabel.text = "click on your card"
            // allows to click on hand cards
            addComponents(switchHandCardButton1, switchHandCardButton2, switchHandCardButton3)
        }
    }
    // buttons to choose one card from hand cards that we want to change
    private val switchHandCardButton1 = Button(
        height = 200,
        width = 120,
        posX = 740,
        posY = 810,
        visual = (ColorVisual.LIGHT_GRAY.apply { transparency = 0.1 })
    ).apply { //animation for explicit choice of card
        onMouseEntered = {
            visual = CompoundVisual(  ColorVisual.GREEN.apply {
                transparency = 0.4
            })
        }
        onMouseExited = {
            visual  = CompoundVisual(  ColorVisual.LIGHT_GRAY.apply {
                transparency = 0.1
            })
        }
        onMouseClicked = {
            removeHandSwapButtons(0)
        }
    }

    private val switchHandCardButton2 = Button(
        height = 200,
        width = 120,
        posX = 890,
        posY = 810,
        visual = (ColorVisual.LIGHT_GRAY.apply { transparency = 0.1 })
    ).apply {
        onMouseEntered = {
            visual = CompoundVisual(  ColorVisual.GREEN.apply {
                transparency = 0.4
            })
        }
        onMouseExited = {
            visual  = CompoundVisual(  ColorVisual.LIGHT_GRAY.apply {
                transparency = 0.1
            })
        }
        onMouseClicked = {
            removeHandSwapButtons(1)
        }
    }

    private val switchHandCardButton3 = Button(
        height = 200,
        width = 120,
        posX = 1040,
        posY = 810,
        visual = (ColorVisual.LIGHT_GRAY.apply { transparency = 0.1 })
    ).apply {
        onMouseEntered = {
            visual = CompoundVisual(  ColorVisual.GREEN.apply {
                transparency = 0.4
            })
        }
        onMouseExited = {
            visual  = CompoundVisual(  ColorVisual.LIGHT_GRAY.apply {
                transparency = 0.1
            })
        }
        onMouseClicked = {
            removeHandSwapButtons(2)
        }
    }
    /** desired card from hand cards was chosen
     * */
    private fun removeHandSwapButtons( switchCard: Int ){
        // save index of chosen hand card
        playerCardIndex = switchCard
        // delete option to choose hand cards
        removeComponents(switchHandCardButton1, switchHandCardButton2, switchHandCardButton3)
        // call to click on table card
        tableTextLabel.text = "click on middle card"
        // allows to choose one from table cards
        addComponents(switchMidCardButton1, switchMidCardButton2, switchMidCardButton3)
    }
    // buttons to choose card from the middle
    private val switchMidCardButton1 = Button(
        height = 200,
        width = 120,
        posX = 740,
        posY = 435,
        visual = (ColorVisual.LIGHT_GRAY.apply { transparency = 0.1 })
    ).apply { //animation for explicit choice of card
        onMouseEntered = {
            visual = CompoundVisual(  ColorVisual.GREEN.apply {
                transparency = 0.4
            })
        }
        onMouseExited = {
            visual  = CompoundVisual(  ColorVisual.LIGHT_GRAY.apply {
                transparency = 0.1
            })
        }
        onMouseClicked = {
            removeTableSwapButtons( 0 )
        }
    }

    private val switchMidCardButton2 = Button(
        height = 200,
        width = 120,
        posX = 890,
        posY = 435,
        visual = (ColorVisual.LIGHT_GRAY.apply { transparency = 0.1 })
    ).apply {
        onMouseEntered = {
            visual = CompoundVisual(  ColorVisual.GREEN.apply {
                transparency = 0.4
            })
        }
        onMouseExited = {
            visual  = CompoundVisual(  ColorVisual.LIGHT_GRAY.apply {
                transparency = 0.1
            })
        }
        onMouseClicked = {
            removeTableSwapButtons( 1 )
        }
    }

    private val switchMidCardButton3 = Button(
        height = 200,
        width = 120,
        posX = 1040,
        posY = 435,
        visual = (ColorVisual.LIGHT_GRAY.apply { transparency = 0.1 })
    ).apply {
        onMouseEntered = {
            visual = CompoundVisual(  ColorVisual.GREEN.apply {
                transparency = 0.4
            })
        }
        onMouseExited = {
            visual  = CompoundVisual(  ColorVisual.LIGHT_GRAY.apply {
                transparency = 0.1
            })
        }
        onMouseClicked = {
            removeTableSwapButtons( 2 )
        }
    }
    /** desired card from the middle was chosen
     * */
    private fun removeTableSwapButtons( switchCard: Int ){
        // save index of chosen table card
        midCardIndex = switchCard
        //delete option to choose table cards
        removeComponents(switchMidCardButton1, switchMidCardButton2, switchMidCardButton3)
        // "call" label cleared
        tableTextLabel.text = ""
        // action buttons can be returned
        addComponents(knockButton, switchAllButton, passButton, switchOneButton)
        // calling from service method with saved indices
        rootService.playerActionService.switchOneCard(midCardIndex, playerCardIndex)
    }
    // allows to knock
    private val knockButton = Button(
        height = 70,
        width = 250,
        posX = 1570,
        posY = 940,
        text = "knock",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 28)
    ).apply {
        onMouseClicked = {
            rootService.playerActionService.knock()
        }
    }
    // text on the table, is shown when somebody has knocked
    // or it´s time to choose cards (for "switch one card")
    private val tableTextLabel = Label(
        height = 60,
        width = 500,
        posX = 1300,
        posY = 850,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 30)
    )
    // allows to pass
    private val passButton = Button(
        height = 70,
        width = 250,
        posX = 1290,
        posY = 940,
        text = "pass",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 28)
    ).apply {
        onMouseClicked = {
            rootService.playerActionService.pass()
        }
    }
    // the very first button
    // without pressing it no actions available
    private val revealCards = Button (
        height = 70,
        width = 250,
        posX = 100,
        posY = 850,
        text = "reveal cards",
        font = Font(color = Color.BLACK, fontWeight = Font.FontWeight.LIGHT, size = 28)
    ).apply {
        onMouseClicked = {
            // for current player cards revealed = true
            rootService.playerActionService.revealCards()
            // show front for hand cards
            currentPlayerHandLayout.forEach { it.showFront() }
            // adds action buttons
            addComponents(switchAllButton, switchOneButton, knockButton, passButton)
            // removes reveal button once was clicked
            removeComponents(this)
        }
    }
    // shows current amount of cards in stack
    private val cardStackCountLabel = Label(
        height = 60,
        width = 200,
        posX = 1175,
        posY = 630,
        font = Font(color = Color.WHITE, fontWeight = Font.FontWeight.LIGHT, size = 25)
    )
    /** initialization of visual representation of cards
     * which cards and for which layout
     * */
    private fun insertCards(cards: List<Card>, layout: LinearLayout<CardView>){
        for(i in cards.indices){
            val cardImageLoader = CardImageLoader()
            val currentCard = CardView(
                height = 200,
                width = 120,
                front = ImageVisual(cardImageLoader.frontImageFor(cards[i].suit, cards[i].value)),
                back = ImageVisual(cardImageLoader.backImage)
            )
            layout.add(currentCard)
        }
    }
    /** creates stack of cards
     * same as insertCards, but for stack and middle cards
     * */
    private fun createStack(stack: MutableList<Card>, tableStack: CardStack<CardView>){
        for (i in stack.indices){
            val cardImageLoader = CardImageLoader()
            val currentCard = CardView(
                height = 200,
                width = 120,
                front = ImageVisual(cardImageLoader.frontImageFor(stack[i].suit, stack[i].value)),
                back = ImageVisual(cardImageLoader.backImage)
            )
            tableStack.add(currentCard)
        }
    }
    /** clears the table from all previous components: buttons, names, layouts
     * when new game was started
     * */
    private fun clearMap(){
        val components = arrayOf(midCardsLayout, cardStackLayout, currentPlayerHandLayout,
            topPlayerHandLayout, leftPlayerHandLayout, rightPlayerHandLayout,
            currentPlayerLabel, topPlayerLabel, leftPlayerLabel, rightPlayerLabel,
            cardStackCountLabel, revealCards, tableTextLabel, switchAllButton, switchOneButton, knockButton, passButton)
        val layouts = arrayOf(midCardsLayout, cardStackLayout, currentPlayerHandLayout,
            topPlayerHandLayout, leftPlayerHandLayout, rightPlayerHandLayout)
        for ( i in layouts ){
            i.clear()
        }
        for ( i in components ){
            removeComponents(i)
        }
        tableTextLabel.text = ""
    }
    /** changes visual representation of current player
     * and moves every player in counterclockwise order
     * same functionality as "next person´s turn"
     * */
    private fun changeCurrentPlayer(
        layouts: MutableList<LinearLayout<CardView>>,
        nameLabels: MutableList<Label>) {
        // save name and cards of player that we want to remove
        val tempCardsList = mutableListOf<List<CardView>>()
        val tempNameList = mutableListOf<String>()
        for ( i in layouts){
            tempCardsList.add(i.components)
            i.clear()
        }
        for(i in nameLabels){
            tempNameList.add(i.text)
        }
        // moving all players positions after change
        for( i in 0 until layouts.size ) {
            layouts[i].addAll(tempCardsList[(i+1) % layouts.size])
            nameLabels[i].text = tempNameList[(i+1) % layouts.size]
            layouts[i].forEach{ CardView -> CardView.showBack() }
        }
        // show front of cards for current player when cards were revealed
        if (rootService.currentGame?.actPlayer?.cardsRevealed!!) {
            currentPlayerHandLayout.forEach{ CardView -> CardView.showFront() }
            return
        }
        // when cards were unrevealed makes player reveal them
        addComponents(revealCards)
        removeComponents(switchAllButton, switchOneButton, knockButton, passButton)
        currentPlayerHandLayout.forEach{ CardView -> CardView.showBack() }
    }
    /** initialisation with two players and background
     * */
    init {
        leftPlayerHandLayout.rotate(90)
        rightPlayerHandLayout.rotate(-90)
        background = ColorVisual(2, 74, 5)
    }
    /** representation of the whole game
     * based on the amount of players
     * */
    override fun refreshAfterStartNewGame() {
        val game = rootService.currentGame
        checkNotNull(game) {"no started game"}
        playerAmount = rootService.currentGame?.playerList?.size
        // clears table from previous game
        clearMap()
        // default components for each game
        addComponents(midCardsLayout, cardStackLayout, tableTextLabel, revealCards)
        // players´ names
        val playerNames = game.playerList
        // representation based on the amount of players
        when(playerAmount) {
            4 -> {
                layouts = mutableListOf( // adds layout for each player
                    currentPlayerHandLayout, leftPlayerHandLayout,
                    topPlayerHandLayout, rightPlayerHandLayout
                )
                nameLabels = mutableListOf( // adds names for each player
                    currentPlayerLabel, leftPlayerLabel,
                    topPlayerLabel, rightPlayerLabel
                )
            }
            3 -> {
                layouts = mutableListOf(
                    currentPlayerHandLayout, leftPlayerHandLayout,
                    rightPlayerHandLayout
                )
                nameLabels = mutableListOf(currentPlayerLabel, leftPlayerLabel, rightPlayerLabel)
            }
            else -> {
                layouts = mutableListOf(currentPlayerHandLayout, topPlayerHandLayout)
                nameLabels = mutableListOf(currentPlayerLabel, topPlayerLabel)
            }
        }
        for (i in 0..playerAmount!!.minus(1)) {
            nameLabels[i].text = playerNames[i].playerName // saves names of players
            addComponents(layouts[i], nameLabels[i]) // shows all layouts and names of players
            insertCards(game.playerList[i].handCards, layouts[i]) // initialisation for hand cards
        }
        currentPlayerHandLayout.forEach { CardView -> CardView.showBack() } //hand cards show back
        insertCards(game.midCards, midCardsLayout) // initialisation for table cards
        midCardsLayout.forEach { CardView -> CardView.showFront() } // table cards show front

        createStack(game.deckCards, cardStackLayout) // initialisation for stack
        // shows amount of cards in stack
        cardStackCountLabel.text = "${rootService.currentGame?.deckCards?.size} cards left"
        addComponents(cardStackCountLabel)
    }
    /** representation of next player´s turn
     * */
    override fun refreshAfterChangeToNextPerson() {
        val game = rootService.currentGame
        checkNotNull(game) {"no started game"}
        // shows all actions with delay to make them explicit
        val delay = DelayAnimation(1000)
        delay.onFinished = {
            // as soon as animation finishes, changes player
            changeCurrentPlayer(layouts, nameLabels)
            unlock()
        }
        lock()
        playAnimation(delay)
    }
    /** representation after all 3 cards were switched
     * */
    override fun refreshAfterSwitchAllCards() {
        val game = rootService.currentGame
        checkNotNull(game) {"no started game"}
        // new visual of table cards
        midCardsLayout.clear()
        insertCards(game.midCards, midCardsLayout)
        midCardsLayout.forEach { CardView -> CardView.showFront() }
        // new visual of hand cards
        currentPlayerHandLayout.clear()
        insertCards(game.actPlayer.handCards, layouts[0])
        currentPlayerHandLayout.forEach { CardView -> CardView.showFront() }
    }
    /** representation after pass
     * */
    override fun refreshAfterPass() {
        val game = rootService.currentGame
        checkNotNull(game) {"no started game"}
        // new visual of table cards
        midCardsLayout.clear()
        insertCards(game.midCards, midCardsLayout)
        midCardsLayout.forEach { CardView -> CardView.showFront() }
        // new visual for stack
        createStack(game.deckCards, cardStackLayout)
        cardStackCountLabel.text = "${rootService.currentGame?.deckCards?.size} cards left"
    }
    /** representation after 1 card was changed
     * */
    override fun refreshAfterSwitchOneCard() {
        val game = rootService.currentGame
        checkNotNull(game) {"no started game"}
        // new visual of table cards
        midCardsLayout.clear()
        insertCards(game.midCards, midCardsLayout)
        midCardsLayout.forEach { CardView -> CardView.showFront() }
        // new visual for hand cards
        currentPlayerHandLayout.clear()
        insertCards(game.actPlayer.handCards, layouts[0])
        currentPlayerHandLayout.forEach { CardView -> CardView.showFront() }
    }
    /** representation after player has knocked
     * */
    override fun refreshAfterKnock() {
        val game = rootService.currentGame
        checkNotNull(game) {"no started game"}
        // shows who knocked
        tableTextLabel.text = "${game.actPlayer.playerName} has knocked"
    }
}